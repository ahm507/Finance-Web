package pf.transaction;

public class TransactionDTO {


    //DO NOT CHANGE. JSON GENERATION BASED ON THAT NAMES
    //FIELD NAMES ARE CORRELATED TO WEB INTERFACE COMPONENTS

    String id;
    private String date;
    String description;
    private double amount;
    private String amountFormated;
    private double balance;
    private String balanceFormated;
    private String withdrawId;
    private String depositId;
    String withdraw;
    String deposit;


    TransactionDTO(TransactionEntity entity) {
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
}
