package pf.charts;

import pf.transaction.TransactionService;

//@Data
public class Balance {
	private double expenses, income, liabilities, assets;
	private String month;

	public Balance(String month, double expenses, double income, double liabilities, double assets) {
		this.setExpenses(expenses);
		this.setIncome(income);
		this.setLiabilities(liabilities);
		this.setAssets(assets);
		this.setMonth(month);
	}

	public double getExpenses() {
		return expenses;
	}

	void setExpenses(double expenses) {
		this.expenses = expenses;
	}

	double getAssets() {
		return assets;
	}

	void setAssets(double assets) {
		this.assets = assets;
	}

	double getIncome() {
		return income;
	}

	void setIncome(double income) {
		this.income = income;
	}

	double getLiabilities() {
		return liabilities;
	}

	void setLiabilities(double liabilities) {
		this.liabilities = liabilities;
	}

	String getMonth() {
		return month;
	}

	void setMonth(String month) {
		this.month = month;
	}

	String format(double number) {
		return TransactionService.formatMoney(number);
	}

	String getExpensesFormatted() {
		return format(expenses);
	}

	String getIncomeFormatted() {
		return format(income);
	}

	String getAssetsFormatted() {
		return format(assets);
	}

	String getLiabilitiesFormatted() {
		return format(liabilities);
	}

	@Override
	public String toString() {
		return "Balance [expenses=" + expenses + ", income=" + income + ", liabilities=" + liabilities + ", assets="
				+ assets + ", month=" + month + "]";
	}

}
