package pf.transaction;

public class TransactionDTO {

	// DO NOT CHANGE. JSON GENERATION BASED ON THAT NAMES
	// FIELD NAMES ARE CORRELATED TO WEB INTERFACE COMPONENTS

	private String id;

	private String date;
	private String description;

	private double amount;
	private String amountFormated;

	private double balance;
	private String balanceFormated;

	private String withdrawId;
	private String depositId;
	private String withdraw;
	private String deposit;

	TransactionDTO(Transaction entity) {
		id = entity.getId();
		date = entity.getDate();
		description = entity.getDescription();
		amount = entity.getAmount();
		setAmountFormated(entity.getAmountFormated());
		setBalance(entity.getBalance());
		setBalanceFormated(entity.getBalanceFormated());
		deposit = entity.getDepositAccount().getText();
		depositId = entity.getDepositId();

		withdrawId = entity.getWithdrawId();
		withdraw = entity.getWithdrawAccount().getText();

	}

	public String getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(String withdraw) {
		this.withdraw = withdraw;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setWithdrawId(String withdrawId) {
		this.withdrawId = withdrawId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}

	public double getBalance() {
		return balance;
	}

	public String getWithdrawId() {
		return withdrawId;
	}

	public String getDepositId() {
		return depositId;
	}

	public double getAmount() {
		return amount;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setAmountFormated(String amountFormated) {
		this.amountFormated = amountFormated;
	}

	public void setBalanceFormated(String balanceFormated) {
		this.balanceFormated = balanceFormated;
	}

	public String getBalanceFormated() {
		return balanceFormated;
	}

	public String getAmountFormated() {
		return amountFormated;
	}
}
