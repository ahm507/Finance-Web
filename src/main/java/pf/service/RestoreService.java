package pf.service;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pf.account.AccountEntity;
import pf.account.AccountRepository;
import pf.account.AccountService;
import pf.transaction.TransactionRepository;
import pf.transaction.TransactionService;

import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Vector;

@Service
public class RestoreService {

	@Autowired
	AccountRepository accountRepo;
	@Autowired
	AccountService accountService;
	@Autowired
	TransactionRepository transRepo;
	@Autowired
	TransactionService transactionService;
	
	
	HashMap<String, String> map = new HashMap<>();
	
	public Vector<String> importFile(String userId, Reader reader) throws Exception {
		CSVReader csvReader = null;
		Vector<String> output = new Vector<>();
		try {
			csvReader = new CSVReader(reader);
			output = importFile(csvReader, userId);
			csvReader.close();
		} finally {
			if(csvReader != null) {
				csvReader.close();
			}
		}
		return output;
	}

	public Vector<String> importFile(String userId, String fileName) throws Exception {
		CSVReader reader = null;
		Vector<String> output = new Vector<>();

		try {
			reader = new CSVReader(new FileReader(fileName));
			output = importFile(reader, userId);
			reader.close();
		} finally {
			if(reader != null) {
				reader.close();
			}
		}
		return output;
	}

	@Transactional
	public Vector<String> importFile(CSVReader reader, String userId) throws Exception {
		String[] nextLine;

		deleteAccountsAndTransactions(userId);

		Vector<String> output = new Vector<>();
		//skip first line of field column name
		reader.readNext();
		int line = 0;
		while ((nextLine = reader.readNext()) != null) {
			if (nextLine[0].isEmpty()) {
				output.add("Finished, next line is blank" + "<br>");
				output.add("Import is finished successfully, I hope :)<br>");
				reader.close();
				return output;
			}
			line++;
			createTransactionAndAccounts(userId, nextLine);
			output.add("Done " + String.valueOf(line) + ":, " + nextLine[0] + ", " + nextLine[1] + "<br>");
		}
		output.add("Import is finished successfully, I hope :)<br>");
		return output;

	}


	private void createTransactionAndAccounts(String userId, String[] segments) throws Exception {
		String date = segments[0];
		String description = segments[1];
		String withdrawName = segments[2];
		String depositName = segments[3];
		String amount = segments[4];
		String currency = segments[5];
		String withdrawId = getAccountId(userId, withdrawName, currency);
		String depositId = getAccountId(userId, depositName, currency);
		amount = amount.replace("\"", "");
		amount = amount.replace(",", "");
		description = description.replace("\"", "");		
		transactionService.saveTransaction(userId, date, description, withdrawId, depositId, amount);
	}

	//Two levels only: This limitation has many side effects, changing it must be tested fully.
	private String getAccountId(String userId, String accountNamePath, String currency) throws Exception {
		//get from hash
		String accountId = map.get(accountNamePath);
		if(accountId != null) {
			return accountId;
		}
		//else get from DB
		String[] seg = accountNamePath.split(":");
		if(seg.length != 2) {
			throw new Exception("Parser Error: " + accountNamePath);
		}
		String strAcc1 = seg[0];
		String strAcc2 = seg[1];
		String type = seg[0]; //.toLowerCase()
		switch (type) {
			case ChartService.CAT_ASSETS:
				type = AccountEntity.ASSET; //singular to match the database
				break;
			case ChartService.CAT_EXPENSES:
				type = AccountEntity.EXPENSE;
				break;
			case ChartService.CAT_INCOME:
				type = AccountEntity.INCOME;
				break;
			case ChartService.CAT_LIABILITIES:
				type = AccountEntity.LIABILITY;
				break;
			case ChartService.CAT_OTHER:
				type = AccountEntity.OTHER;
				break;
			default:
				throw new Exception("Parent Account must be one of 'Assets/Expenses/Income/Liability/Other', " + type + " is invalid!");
		}
		
		String parent = "0";
		String id = map.get(strAcc1);
		if(id == null) {
//			AccountEntity acc1 = accountRepo.findWithAccountNameAndParentId(userId, strAcc1, parent);
			AccountEntity acc1 = accountRepo.findByUser_IdAndIdAndParent(userId, strAcc1, parent);
			
			if(acc1 == null) {
				String acc1UUID = accountService.createWithParent(userId, strAcc1, "", type, parent, currency);
				acc1 = accountRepo.findByUser_IdAndId(userId, acc1UUID);
			}
			map.put(strAcc1, acc1.getId());
			parent = String.valueOf(acc1.getId());
		} else {
			parent = id;
		}
		//here is the component 2
//		AccountEntity acc2 = accountRepo.findWithAccountNameAndParentId(userId, strAcc2, parent);
		AccountEntity acc2 = accountRepo.findByUser_IdAndIdAndParent(userId, strAcc2, parent);
		if(acc2 == null) {
			String acc2UUID = accountService.createWithParent(userId, strAcc2, "", type, parent, currency);
			acc2 = accountRepo.findByUser_IdAndId(userId, acc2UUID);
		}
		map.put(accountNamePath, String.valueOf(acc2.getId()));
		return acc2.getId();
	}

	@Transactional
	private void deleteAccountsAndTransactions(String userId) throws Exception {
		transRepo.deleteByUser_Id(userId);
		accountRepo.deleteByUser_Id(userId);
	}
}
