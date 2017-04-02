package pf.backup;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import pf.account.Account;
import pf.account.AccountService;
import pf.account.DeepAccountLayersException;
import pf.account.NullAccountException;
import pf.email.Mailer;
import pf.transaction.Transaction;
import pf.transaction.TransactionRepository;
import pf.user.User;
import pf.user.UserRepository;

//import pf.db.AccountStoreJdbc;

//Called from JSP
//It returns CSV file and should be clean

@Service
public class BackupService {

	
	private static final Logger log = LoggerFactory.getLogger(BackupService.class);

	private AccountService accountService;
	private TransactionRepository transRepo;
	private UserRepository userRepository;
	private String batchReportsFolder;
	private SpringTemplateEngine templateEngine;
	private Mailer mailer;
	
	public BackupService(AccountService accountService, TransactionRepository transRepo, UserRepository userRepository, 
			@Value("${pf.service.weekly-report.folder:/Users/Macpro/Server/pf-batch-reports/}") String batchReportsFolder, 
			SpringTemplateEngine templateEngine, Mailer mailer) {
		this.accountService = accountService;
		this.transRepo = transRepo;
		this.userRepository = userRepository;
		this.batchReportsFolder = batchReportsFolder;
		this.templateEngine = templateEngine;
		this.mailer = mailer;
	}

	String userId;

	public List<Transaction> backupUserData(String userId)
			throws NullAccountException, DeepAccountLayersException {
		this.userId = userId;
		List<Transaction> transes = transRepo.findByUser_Id(userId);
		// Use HashMap as a cache
		HashMap<String, Account> map = new HashMap<>();
		for (Transaction t : transes) {
			// WithdrawPath Acc1:Acc2:Acc3
			t.setWithdrawAccountPath(
					accountService.getAccountPath(userId, t.getWithdrawId(), t.getWithdrawAccount().getText(), map));
			t.setDepositAccountPath(
					accountService.getAccountPath(userId, t.getDepositId(), t.getDepositAccount().getText(), map));
		}
		return transes;
	}

	public String getTitleFormatted() {
		return "Date, Description, Account, Transfer to, Amount, Currency";
	}

	public String getRowFormatted(Transaction t) throws CurrencyTransefereException {
		Account account1 = t.getWithdrawAccount();
		Account account2 = t.getDepositAccount();

		if (!account1.getCurrency().equals(account2.getCurrency())) {
			throw new CurrencyTransefereException(
					"Data Error: transfer between two different currencies is not allowed - " + account1.getCurrency()
							+ " & " + account2.getCurrency());
		}
		return t.getDate() + ",\"" + t.getDescription() + "\"," + t.getWithdrawAccountPath() + ","
				+ t.getDepositAccountPath() + "," + t.getAmount() + "," + account1.getCurrency();
	}

	public String getFileName() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return "pf-backup-" + dateFormat.format(date) + ".csv";
	}

	public void backup(String userId, PrintWriter printWriter) throws Exception {
		printWriter.println(getTitleFormatted());
		List<Transaction> ts = backupUserData(userId);
		for (Transaction t : ts) {
			printWriter.println(getRowFormatted(t));
		}
	}

	public String getExportContents(String userId)
			throws NullAccountException, DeepAccountLayersException, CurrencyTransefereException {
		List<Transaction> ts = backupUserData(String.valueOf(userId));

		StringBuilder buffer = new StringBuilder();
		buffer.append(getTitleFormatted()).append("\r\n");
		for (Transaction t : ts) {
			buffer.append(getRowFormatted(t)).append("\r\n");
		}
		String contents = buffer.toString();
		return contents;
	}

	
///////////////////////////
	String fullPathName;
	//Weekly backup
//	@Scheduled(cron = "0 0 0 */5 * *") // Every 5 days

	@Scheduled(cron = "0 0 0 * * *") // Every day as a testing period
	public void autoBackup() throws Exception {
		Iterable<User> allUsers = userRepository.findAll();// OrderByEmail
		for (User user : allUsers) {
			String userEmail = user.getEmail();
			autoBackupForUser(user.getId(), userEmail);
		}
	}

	public void autoBackupForUser(String email) throws FileNotFoundException, UnsupportedEncodingException, NullAccountException, DeepAccountLayersException, CurrencyTransefereException, IOException, Exception {
		User userEntity = userRepository.findByEmail(email);
		autoBackupForUser(userEntity.getId(), email);
	}
	
	private void autoBackupForUser(String userId, String userEmail)
			throws NullAccountException, DeepAccountLayersException, CurrencyTransefereException, FileNotFoundException,
			IOException, UnsupportedEncodingException, Exception {
		String fullFileName = userEmail + "-" + getFileName();
		this.fullPathName = batchReportsFolder + fullFileName; 
		//FIXME: Unify into Export/Backup one naming system
		String contents = getExportContents(userId);
		String zipFileName = fullPathName + ".zip";
		ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
		
		ZipEntry entry = new ZipEntry(fullFileName);
		zip.putNextEntry(entry);
		zip.write(contents.getBytes("utf-8"));
		zip.close();
		
		//send it attached into email
		String htmlEmail = renderEmailTemplate(userEmail);
		String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String subject = String.format("Weekly Backup as of %s ", dateStamp);
		mailer.sendBackupEmail(userEmail, subject, htmlEmail, zipFileName);
	}

	public String getPeridicFileFullPath() {
		return fullPathName;
	}
	
	
	private String renderEmailTemplate(String email) {
		Context thymeleafCtx = new Context();// locale!
		thymeleafCtx.setVariable("USER_NAME", email);
		// settings add ".html" and uses /resources/templates
		String htmlContent = templateEngine.process("email-template-backup", thymeleafCtx);
		log.info(String.format("email-template-backup.html template is populated successfully for user %s.", email));
		return htmlContent;
	}
	
}
