package pf.backup;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import pf.account.AccountEntity;
import pf.account.AccountService;
import pf.account.DeepAccountLayersException;
import pf.account.NullAccountException;
import pf.email.Mailer;
import pf.transaction.TransactionEntity;
import pf.transaction.TransactionRepository;
import pf.user.UserEntity;
import pf.user.UserRepository;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

	public List<TransactionEntity> backupUserData(String userId)
			throws NullAccountException, DeepAccountLayersException {
		this.userId = userId;
		List<TransactionEntity> transes = transRepo.findByUser_Id(userId);
		// Use HashMap as a cache
		HashMap<String, AccountEntity> map = new HashMap<>();
		for (TransactionEntity t : transes) {
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

	public String getRowFormatted(TransactionEntity t) throws CurrencyTransefereException {
		AccountEntity account1 = t.getWithdrawAccount();
		AccountEntity account2 = t.getDepositAccount();

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
		List<TransactionEntity> ts = backupUserData(userId);
		for (TransactionEntity t : ts) {
			printWriter.println(getRowFormatted(t));
		}
	}

	public String getExportContents(String userId)
			throws NullAccountException, DeepAccountLayersException, CurrencyTransefereException {
		List<TransactionEntity> ts = backupUserData(String.valueOf(userId));

		StringBuilder buffer = new StringBuilder();
		buffer.append(getTitleFormatted()).append("\r\n");
		for (TransactionEntity t : ts) {
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
		//for all users
			//do backup
		
		Iterable<UserEntity> allUsers = userRepository.findAll();// OrderByEmail
		for (UserEntity user : allUsers) {
			String userEmail = user.getEmail();
			
			String fullFileName = userEmail + "-" + getFileName();
			this.fullPathName = batchReportsFolder + fullFileName; 
			//FIXME: Unify into Export/Backup one naming system
			String contents = getExportContents(user.getId());
			ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fullPathName + ".zip")));
			
			ZipEntry entry = new ZipEntry(fullFileName);
			zip.putNextEntry(entry);
			zip.write(contents.getBytes("utf-8"));
			zip.close();
			
			//send it attached into email
			String htmlEmail = renderEmailTemplate(userEmail);
			String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String subject = String.format("Weekly Backup as of %s ", dateStamp);
			mailer.sendBackupEmail(userEmail, subject, htmlEmail, fullPathName);
			
		}
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
