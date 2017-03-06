package pf.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pf.account.AccountEntity;
import pf.account.AccountRepository;
import pf.service.MinMaxDate;
import pf.user.UserEntity;
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
	
	@Autowired UserRepository userRepo;
	@Autowired AccountRepository accountRepo;
	@Autowired TransactionRepository transRepo;
	
//	private String id;
//	private String withdrawId;
//	private String depositId;
//	private String date;
//	private String description;
//	private double amount;
//	private String withdraw; //came from Account table
//	private String deposit;
//	//FIXME: Account withdraw, deposit; //association
//
//	private Transaction() {
//	}

	//Just for unit test usage
//	public Transaction(String id, String date, String description, String withdrawId, String depositId, double amount) {
////		super(id, date, description, withdrawId, depositId, amount);
//		this.id = id;
//		this.date = date;
//		this.description = description;
//		this.withdrawId = withdrawId;
//		this.depositId = depositId;
//		this.amount = amount;
//	}

//	public Transaction(TransactionRepository record) {
//		this.id = record.getId();
//		this.withdrawId = record.getWithdrawId();
//		this.depositId = record.getDepositId();
//		this.date = record.getDate();
//		this.description = record.getDescription();
//		this.amount = record.getAmount();
//		this.withdraw = record.getWithdraw_id();
//		this.deposit = record.getDeposit_id();
//	}

//	public Transaction(AccountRepository accountDao, TransactionRepository transactionDao) {
//		this.accountrepo = accountDao;
//		this.transRepo = transactionDao;
//	}

//	private List<TransactionEntity> mapStoreArray(TransactionRepository[] records) {
//		List<TransactionEntity> transactions = new Transaction[records.length];
//		int i = 0;
//		for (TransactionRepository record : records) {
//			transactions[i] = new Transaction(record);
//			i++;
//		}
//		return transactions;
//	}

	static public String formatMoney(double number) {
		//Format the numbers for display as example "100,050,676.574";
		if (number == 0) { //double have some error factor
			return "0.00";
		}

		//FIXME: Use Formatter class, as ex, fmt.format("%,.2f", 4356783497.34);
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

		List<TransactionEntity> transactions = transRepo.queryByUserAndAccountOrderByDate(userRepo.findById(userId),
				accountRepo.findById(accountId));
		AccountEntity account = accountRepo.findByUser_IdAndId(userId, accountId);
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
		AccountEntity withdrawAccount = accountRepo.findByUser_IdAndId(userId, withdrawId);
		AccountEntity depositAccount = accountRepo.findByUser_IdAndId(userId, depositId);
		if(  !  withdrawAccount.getCurrency().equals(depositAccount.getCurrency())) {
			String error = "Unable to transfer amount between different accounts:" +
					withdrawAccount.getText() + " " + withdrawAccount.getCurrency() + " & " +
					depositAccount.getText() + " " + depositAccount.getCurrency();
			throw new Exception(error);
		}

		//Add time to date string
		//if there is no time component; because it is used from import and from brand new transaction creation.
		//Usually space separates between data and time
		//fixme: this way of handling dates as strings is not locale safe
		if(date.indexOf(" ") == -1) {
			date += concatTimeNow();
		}

		TransactionEntity transEntity = new TransactionEntity(uuid,
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
		TransactionEntity trans = transRepo.queryById(id);
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
		TransactionEntity trans = transRepo.queryById(id);
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
		UserEntity user = userRepo.findById(userId);
		AccountEntity account = accountRepo.findByUser_IdAndId(userId, accountId);
//		Date dateFromObject = new Date();
//		SimpleDateFormat ft =
//				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		List<TransactionEntity> transactions = transRepo.queryByUserAndDateBetweenAndAccountOrderByDate
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
			case AccountEntity.INCOME:
				return -1;
			case AccountEntity.ASSET:
				return 1;
			case AccountEntity.EXPENSE:
				return 1;
			default:  // if(type.equals("liabilities")) {
				return -1;
		}
	}

	int getWithdrawSign(String type) {
		switch (type) {
			case AccountEntity.INCOME:
				return 1;
			case AccountEntity.ASSET:
				return -1;
			case AccountEntity.EXPENSE:
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
