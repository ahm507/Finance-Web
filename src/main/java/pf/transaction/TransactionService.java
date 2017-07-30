package pf.transaction;

import org.springframework.stereotype.Service;
import pf.account.Account;
import pf.account.AccountRepository;
import pf.backup.MinMaxDate;
import pf.user.User;
import pf.user.UserRepository;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionService {

	//calculated fields
//	public double balance;
//	public String amountFormated, balanceFormated; //formatted as 12,122,343.00

	//DO NOT CHANGE. JSON GENERATION BASED ON THAT NAMES
	//FIELD NAMES ARE CORRELATED TO WEB INTERFACE
	
	UserRepository userRepo;
	AccountRepository accountRepo;
	TransactionRepository transRepo;
	public TransactionService(UserRepository userRepo, AccountRepository accountRepo, TransactionRepository transRepo) {
		this.userRepo = userRepo;
		this.accountRepo = accountRepo;
		this.transRepo = transRepo;
	}
	
	static public String formatMoney(double number) {
		//Format the numbers for display as example "100,050,676.574";
		if (number == 0) { //double have some error factor
			return "0.00";
		}

		DecimalFormat formatter = new DecimalFormat("#,###.00");
		return formatter.format(number);
	}

	public Map<String, Object> getTransactionsMap(String userId, String accountId) throws Exception {
		List<TransactionDTO> transactions = getTransactions(userId, accountId);
		return packageToMap(transactions); //empty result
	}

	public List<TransactionDTO> getTransactions(String userId, String accountId) throws Exception {
		if(accountId == null || accountId.isEmpty()) {
			return new ArrayList<>(); //empty result
		}

		List<Transaction> transactions = transRepo.queryByUserAndAccountOrderByDate(userRepo.findById(userId),
				accountRepo.findById(accountId));
		Account account = accountRepo.findByUser_IdAndId(userId, accountId);
		if(account == null) { //there is no account with this ID
			List<TransactionDTO> dto = new ArrayList<>();
			transactions.stream().forEach(t -> dto.add(new TransactionDTO(t)));
			return dto;
		}

		List<TransactionDTO> dto = new ArrayList<>();
		transactions.stream().forEach(t -> dto.add(new TransactionDTO(t)));

		computeBalance(accountId, account.getType(), dto);
		formatMoney(dto);
		return dto;
	}

	public void saveTransaction(String userId, String date, String description, String withdrawId, String depositId,
								String amount) throws Exception {
		String cleanAmount = amount.replace(",", "");
		String uuid = UUID.randomUUID().toString();

		//verify no transfer between accounts of different currencies
		Account withdrawAccount = accountRepo.findByUser_IdAndId(userId, withdrawId);
		Account depositAccount = accountRepo.findByUser_IdAndId(userId, depositId);
		
		if(withdrawAccount == null) throw new Exception("withdrawAccount is null for userId:" + userId + ", and withdrawID=" + withdrawId);
		if(depositAccount == null) throw new Exception("depositAccount is null for userId:" + userId + ", and depositId=" + depositId);
		
		if(  !  withdrawAccount.getCurrency().equals(depositAccount.getCurrency())) {
			String error = "Unable to transfer amount between different accounts:" +
					withdrawAccount.getText() + " " + withdrawAccount.getCurrency() + " & " +
					depositAccount.getText() + " " + depositAccount.getCurrency();
			throw new Exception(error);
		}

		//Add time to date string
		//if there is no time component; because it is used from import and from brand new transaction creation.
		//Usually space separates between data and time
		//FIXME: this way of handling dates as strings is not locale safe
		if(date.indexOf(" ") == -1) {
			date += concatTimeNow();
		}

		Transaction transEntity = new Transaction(uuid,
				userRepo.findById(userId),
				date, description,
				accountRepo.findByUser_IdAndId(userId, withdrawId),
//				depositId,
				accountRepo.findByUser_IdAndId(userId, depositId),
				Double.parseDouble(cleanAmount));
		transRepo.save(transEntity);
	}

	private String concatTimeNow() {
		return new SimpleDateFormat(" HH:mm:ss").format(new Date());
	}

	public void updateTransaction(String id, String date, String description, String withdraw, String deposit, String amount) throws Exception {
		String amountCleared = amount.replace(",", "");
		Transaction trans = transRepo.queryById(id);
		//Add time component if needed
		//date comes from UI without leading zeros
//		String today = new SimpleDateFormat ("yyyy-M-d").format(new Date());
//		if(today.equals(date)) {
		//No time comes from UI
//		date += concatTimeNow();
//		}

		//Initial creation from UI has no date from UI but I add it in service layer.
		//time editing with date works - as long as there is a time.
		if (date.indexOf(' ') == -1) { //there is no time component
			//Add time to date string
			date += concatTimeNow();
		}


		trans.setDate(date); //time will be set to 0:0:0 automatically
		trans.setDescription(description);
		trans.setWithdrawAccount(accountRepo.findById(withdraw)); //id is uuid
		trans.setDepositAccount(accountRepo.findById(deposit)); //id is uuid
		trans.setAmount(Double.parseDouble(amountCleared));
		transRepo.save(trans);
	}

	public void removeTransaction(String id) throws Exception {
		Transaction trans = transRepo.queryById(id);
		transRepo.delete(trans);
	}

	public List<TransactionDTO> getMonthTransactions(String userId, String accountId, String year, String month) throws Exception {
		//month sent is zero based
		if(accountId == null || "".equals(accountId) || month == null || "".equals(month) ) {
			return new ArrayList<>(); //empty result
		}
		int m = Integer.parseInt(month);
		String monthAfter = String.valueOf(m+1);
		String fromDate = year + "-" + monthAfter + "-01";
		String toDate = year + "-" + monthAfter + "-31";
//		List<TransactionDTO> dto = new ArrayList<>();
		return getTransactions(userId, accountId, fromDate, toDate);
	}

	//Get single year transaction
	public List<TransactionDTO> getYearTransactions(String userId, String accountId, String year) throws Exception {
		//month sent is zero based
		List<TransactionDTO> dto = new ArrayList<>();

		if(accountId == null || "".equals(accountId)) {
			return dto; //empty result
		}
		String fromDate = year + "-01-01";
		String toDate = year + "-12-31";

		return getTransactions(userId, accountId, fromDate, toDate);
	}

	//Get single year transaction
	public List<TransactionDTO> getYearTransactions(String userId, String year) throws Exception {
		String fromDate = year + "-01-01";
		String toDate = year + "-12-31";
//		List<TransactionEntity> transactions = transRepo.findByUser_IdAndDateBetween(userId, fromDate, toDate);
//		return transactions;
		List<TransactionDTO> dto = new ArrayList<>();
		transRepo.findByUser_IdAndDateBetweenOrderByDate(userId, fromDate, toDate).
				forEach(t -> dto.add(new TransactionDTO(t)));
		return dto;

	}

	public List<TransactionDTO> getUpToMonthTransactions(String userId, String accountId, String year, String month) throws Exception {
		//month sent is zero based
		List<TransactionDTO> dto = new ArrayList<>();
		if(accountId == null || "".equals(accountId) || month == null || "".equals(month) ) {
			return dto; //empty result
		}
		int m = Integer.parseInt(month);
		String monthOneBased = String.valueOf(m+1);
		String fromDate = "1900-01-01"; //just very early date
		String toDate = year + "-" + monthOneBased + "-31";
//		return  getTransactions(userId, accountId, fromDate, toDate);
		return getTransactions(userId, accountId, fromDate, toDate);
	}

	public List<TransactionDTO> getUpToYearTransactions(String userId, String accountId, String year) throws Exception {
		//month sent is zero based
		if(accountId == null || "".equals(accountId)) {
			return new ArrayList<>(); //empty result
		}
		String fromDate = "1900-01-01"; //just very early date
		String toDate = year + "-12-31";
		List<TransactionDTO>  ts = getTransactions(userId, accountId, fromDate, toDate);
		List<TransactionDTO>  filteredTs = new ArrayList<>();

		//Now after proper balance calculations, simply truncate older records that the start of this year
//		List<TransactionEntity> filteredTs = new ArrayList<TransactionEntity>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		for(TransactionDTO t : ts) {
			String yearBefore = String.valueOf(Integer.parseInt(year)-1);
			Date queryYearStart = dateFormatter.parse(yearBefore + "-12-31"); //last day in previous year
			Date transactionDate = dateFormatter.parse(t.getDate());
			if(transactionDate.after(queryYearStart)) {
//				filteredTs.add(t);
				filteredTs.add(t);
			}

		}
		//it will allocate a new array if the passed is not sufficient ;)
//		return filteredTs.toArray(new Transaction[filteredTs.size()]);
		return filteredTs;
	}

	public List<String> getYearList(String email) throws Exception {
		//Map<String, Object> yearRange = transactionStore.findYearRange(userId);
//		MinMaxDate yearRange = transRepo.findYearRange(userId);
		String minMax = transRepo.queryMinAndMaxDate(userRepo.findByEmail(email));
		String[] minMaxArray = minMax.split(",");
		MinMaxDate minMaxDate = null;
		if(minMaxArray.length > 0 && minMaxArray[0].equals("null") == false) {
			minMaxDate = new MinMaxDate(minMaxArray[0], minMaxArray[1]);
		}
		return generateYearList(minMaxDate);
	}

	public List<String> generateYearList(MinMaxDate yearRange) {
		if(yearRange == null) {
			return new ArrayList<>(); //just empty list
		} else {
			String minDate = yearRange.getMin();
			String maxDate = yearRange.getMax();
			if(minDate == null || maxDate == null) {
				return new ArrayList<>(); //just empty list
			}
			String minYear = minDate.substring(0, 4);
			String maxYear = maxDate.substring(0, 4);
			int start = Integer.parseInt(minYear);
			int end = Integer.parseInt(maxYear);
			ArrayList<String> years = new ArrayList<>();
			for(int y = start; y <= end; y++) {
				years.add(String.valueOf(y));
			}
			return years;
		}
	}

	public List<TransactionDTO> getTransactions(String userId, String accountId, String fromDate, String toDate) throws Exception {
		//month sent is zero based
		List<TransactionDTO> dto = new ArrayList<>();
		if(accountId == null || "".equals(accountId)) {
			return dto; //just empty list
		}
		User user = userRepo.findById(userId);
		Account account = accountRepo.findByUser_IdAndId(userId, accountId);
//		Date dateFromObject = new Date();
//		SimpleDateFormat ft =
//				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		List<Transaction> transactions = transRepo.queryByUserAndDateBetweenAndAccountOrderByDate
				(user, fromDate, toDate, account);
		transactions.forEach(t -> dto.add(new TransactionDTO(t)));
		computeBalance(accountId, account.getType(), dto);
		formatMoney(dto);
		return dto;
	}

	private void formatMoney(List<TransactionDTO> transactions) {
		for(TransactionDTO t : transactions) {
			t.setAmountFormated(formatMoney(t.getAmount()));
			t.setBalanceFormated(formatMoney(t.getBalance()));
		}
	}

	//Get month transactions for certain user and for all accounts for performance optimization
	public List<TransactionDTO> getYearMonthTransactions(String userId, String year, int month) throws Exception {
		String fromDate = year + "-" + String.valueOf(month) + "-01";
		String toDate = year + "-" + String.valueOf(month) + "-31";
		List<TransactionDTO> dto = new ArrayList<>();

		transRepo.findByUser_IdAndDateBetweenOrderByDate(userId, fromDate, toDate).
				forEach(t -> dto.add(new TransactionDTO(t)));

		return dto;
	}

	public double getAccountBalance(String userId, String accountId) throws Exception {
		List<TransactionDTO> transactions = getTransactions(userId, accountId);
		if(transactions.size() > 0) {
			return transactions.get(transactions.size()-1).getBalance(); //the balance of last transaction
		}
		else {
			return 0;
		}
	}

	int getDepositSign(String type) {
		switch (type) {
			case Account.INCOME:
				return -1;
			case Account.ASSET:
				return 1;
			case Account.EXPENSE:
				return 1;
			default:  // if(type.equals("liabilities")) {
				return -1;
		}
	}

	int getWithdrawSign(String type) {
		switch (type) {
			case Account.INCOME:
				return 1;
			case Account.ASSET:
				return -1;
			case Account.EXPENSE:
				return -1;
			default:  // if(type.equals("liabilities")) {
				return 1;
		}
	}

	public double computeBalance(String accountId, String type, List<TransactionDTO> transes) {
		double bal = 0;
		int factor;

		for(TransactionDTO trans: transes) {
			if(trans.getWithdrawId().equals(accountId)) {
				factor = getWithdrawSign(type);
				bal += trans.getAmount() * factor;
			}
			else if(trans.getDepositId().equals(accountId)) {
				factor = getDepositSign(type);
				bal += trans.getAmount() * factor;
			}
//			trans.balance += bal;
			trans.setBalance(trans.getBalance() + bal);

		}
		return bal;
	}

	private Map<String, Object> packageToMap(List<TransactionDTO> transactions) {
		Map<String, Object> map = new HashMap<>();
		map.put("total", String.valueOf(transactions.size()));
		map.put("rows", transactions);
		return map;
	}


}
