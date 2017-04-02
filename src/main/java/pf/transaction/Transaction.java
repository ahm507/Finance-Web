package pf.transaction;

import pf.account.Account;
import pf.user.User;

import javax.persistence.*;

@Entity
@Table(name="transaction")
public class Transaction {
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)

	private String id;
	private String date;
	private String description;
	private double amount; 

	@Transient private String withdrawAccountPath;
	@Transient private String depositAccountPath;

	@Transient private double balance;
	@Transient private String amountFormated;
	@Transient private String balanceFormated; //formatted as 12,122,343.00

	@ManyToOne(optional = false, targetEntity=User.class)
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

	@ManyToOne(optional = false, targetEntity=Account.class)
	@JoinColumn(name = "withdrawId", referencedColumnName = "id")
	private Account withdrawAccount;

	@ManyToOne(optional = false, targetEntity=Account.class)
	@JoinColumn(name = "depositId", referencedColumnName = "id")
	private Account depositAccount;

	//Default constructor is required by Hibernate
	public Transaction() {
		
	}
	
	
//	transRepo.insert(uuid, userId, date, description, withdrawId, depositId, cleanAmount);
public Transaction(String id, User user, String date, String description,
                         Account withdraw, Account deposit,
                         double amount) {
		this.id = id;
		this.user = user;
		this.date = date;
		this.description = description;
		this.withdrawAccount = withdraw;
		this.depositAccount = deposit;
		this.amount = amount;
	}
	

	public void setAmount(double amount) {
		this.amount =amount;  		
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Account getWithdrawAccount() {
		return withdrawAccount;
	}

	public void setWithdrawAccount(Account withdrawAccount) {
		this.withdrawAccount = withdrawAccount;
	}

	public Account getDepositAccount() {
		return depositAccount;
	}

	public void setDepositAccount(Account deposit) {
		this.depositAccount = deposit;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public double getBalance() {
		return balance;
	}


	public void setBalance(double balance) {
		this.balance = balance;
	}


	public String getAmountFormated() {
		return amountFormated;
	}


	public void setAmountFormated(String amountFormated) {
		this.amountFormated = amountFormated;
	}


	public String getBalanceFormated() {
		return balanceFormated;
	}


	public void setBalanceFormated(String balanceFormated) {
		this.balanceFormated = balanceFormated;
	}


	public String getWithdrawId() {
		
		return getWithdrawAccount().getId();
	}


	public String getDepositId() {
		
		return getDepositAccount().getId();
	}


	public String getWithdrawAccountPath() {
		return withdrawAccountPath;
	}


	public void setWithdrawAccountPath(String withdrawAccountPath) {
		this.withdrawAccountPath = withdrawAccountPath;
	}


	public String getDepositAccountPath() {
		return depositAccountPath;
	}


	public void setDepositAccountPath(String depositAccountPath) {
		this.depositAccountPath = depositAccountPath;
	}


	
}
