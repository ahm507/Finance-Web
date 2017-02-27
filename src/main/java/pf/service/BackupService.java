package pf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pf.account.AccountEntity;
import pf.account.AccountService;
import pf.transaction.TransactionEntity;
import pf.transaction.TransactionRepository;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import pf.db.AccountStoreJdbc;

//Called from JSP
//It returns CSV file and should be clean

@Service
public class BackupService {

	@Autowired
	AccountService accountService;

	
	@Autowired TransactionRepository transRepo;
	String userId;


	public List<TransactionEntity> backupUserData(String userId) throws Exception { 
		this.userId = userId;
		List<TransactionEntity> transes = transRepo.findByUser_Id(userId);
		//Use HashMap as a cache
		HashMap<String, AccountEntity> map = new HashMap<>();
		for(TransactionEntity t : transes) {
			//WithdrawPath Acc1:Acc2:Acc3
			t.setWithdrawAccountPath(accountService.getAccountPath(userId, t.getWithdrawId(), t.getWithdrawAccount().getText(), map));
			t.setDepositAccountPath(accountService.getAccountPath(userId, t.getDepositId(), t.getDepositAccount().getText(), map));
		}
		return transes;
	}
	
	public String getTitleFormatted() {
		return "Date, Description, Account, Transfer to, Amount, Currency";
	}

	public String getRowFormatted(TransactionEntity t) throws Exception {
		AccountEntity account1 = t.getWithdrawAccount();
		AccountEntity account2 = t.getDepositAccount();

		if (!account1.getCurrency().equals(account2.getCurrency())) {
			throw new Exception("Data Error: transfer between two different currencies is not allowed - " +
					account1.getCurrency() + " & " + account2.getCurrency());
		}
		return t.getDate() + ",\"" + t.getDescription() + "\"," +
				t.getWithdrawAccountPath() + "," + t.getDepositAccountPath() + "," +
				t.getAmount() + "," + account1.getCurrency();
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



}