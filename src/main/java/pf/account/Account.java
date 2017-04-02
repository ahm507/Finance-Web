package pf.account;

import pf.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account  {
	//Constants
	//transient and @transient can be used
	//FIXME: make type an enum
//	public enum Type {
////		income, expense, liability, asset, other;
//	}
	public final static String INCOME = "income";
	public final static String EXPENSE = "expense";
	public final static String LIABILITY = "liability";
	public final static String ASSET = "asset";
	public final static String OTHER = "other";

	public final static String ROOT_ACCOUNT_ID = "0";
	public final static String USD = "usd";
	public final static String SAR = "sar";
	public final static String EGP = "egp"; //Default

	@Id
	// primary key: I handle key generation myself
	private String id;

	@ManyToOne(optional = false, targetEntity=User.class)
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;
// private String userId;


//	public static final AccountEntity ROOT_ACCOUNT = new AccountEntity();

	private String parent;


	private String text;
	//TODO: type as table
	private String type;
	private String description;
	//TODO: currency as table
	private String currency;


	//transient and @transient can be used
	
	// calculated: Not stored in database 
	@Transient private List<Account> children = new ArrayList<>();
	@Transient private double balance;
	@Transient private String balanceFormated;

	//Default constructor is required by hibernate
	public Account() {
		
	}
	
	public Account(String uuid, String parentId, User user, String name, String description, String type, String currency) {
		this.parent = parentId;
		this.id = uuid;
		this.user = user;
		this.text = name;
		this.description = description;
		this.type = type;
		this.currency = currency;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public User getUserId() {
		return user;
	}

	public void setUserId(User user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<Account> getChildren() {
		return children;
	}

	public void setChildren(List<Account> children) {
		this.children = children;
	}

	public String getBalanceFormated() {
		return balanceFormated;
	}

	public void setBalanceFormated(String balanceFormated) {
		this.balanceFormated = balanceFormated;
	}
}
