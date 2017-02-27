package pf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pf.account.AccountEntity;
import pf.account.AccountRepository;
import pf.account.AccountService;
import pf.transaction.TransactionDTO;
import pf.transaction.TransactionService;
import pf.user.UserRepository;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ChartService {

    static public final String CAT_INCOME = "Income";
    static public final String CAT_LIABILITIES = "Liabilities";
   
    static public final String CAT_EXPENSES = "Expenses";
    static public final String CAT_ASSETS = "Assets";
    static public final String CAT_OTHER = "Other";
    final String TOTALS = "totals";
    @Autowired
    AccountRepository accountRepo;
    @Autowired
    UserRepository userRepo = null;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    double usdRate;
    double sarRate;

    public List<Map<String, String>> getChartFields(String type, String userId) throws Exception {
        List<Map<String, String>> out;
        if(type.equals(TOTALS)) {
            out = getTotalsDataFields();
        } else {
            out = getChartDataFields(userId, type);
        }
        return out;
    }


    public List<Map<String, Object>> getExpensesTrend(String year, String type, String userId, String email) throws Exception {
//        this.storeFactory = getStoreFactory();
//        configure();
        List<Map<String, Object>> out2 = new ArrayList<>();
        if("all".equals(year)) {
            Map<String, Map<String, Object>> out = getTrendDataAllYears(userId, email, type);
            //convert map to array for html/JS compatibility
            Set<String> keys = out.keySet();
            for(String key : keys) {
                out2.add(out.get(key));
            }
        } else {
            out2 = getTrendData(userId, email, year, type);
        }
        return out2;
    }



    public Map<String, Map<String, Object>> getTrendDataAllYears(String userId, String email,
                                                                 String type) throws Exception {

        usdRate = userRepo.findByEmail(email).getUsd_rate();
        sarRate = userRepo.findByEmail(email).getSar_rate();

        return getTotalsAllYearsAllAccountTypes(userId);

    }


    public List<Map<String, Object>> getTrendData(String userId, String email, String year,
                                                  String type) throws Exception {

        usdRate = userRepo.findByEmail(email).getUsd_rate();
        sarRate = userRepo.findByEmail(email).getSar_rate();

        if (type.equals("totals")) {
            return getTrendDataTotals(userId, year);
        }
        //Asset and Liabilities
        if (type.equals(AccountEntity.ASSET) || type.equals(AccountEntity.LIABILITY)) {
            return getTrendDataBalances(userId, year, type);
        }
        //income and expenses
        return getDataSummation(userId, year, type);
    }

    public List<Map<String, String>> getTotalsDataFields() {
        List<Map<String, String>> accs = new ArrayList<>();
        HashMap<String, String> asset = new HashMap<>();
        asset.put("name", CAT_ASSETS);
        asset.put("id", AccountEntity.ASSET);
        accs.add(asset);
        HashMap<String, String> income = new HashMap<>();
        income.put("name", CAT_INCOME);
        income.put("id", AccountEntity.INCOME);
        accs.add(income);
        HashMap<String, String> expense = new HashMap<>();
        expense.put("name", CAT_EXPENSES);
        expense.put("id", AccountEntity.EXPENSE);
        accs.add(expense);
        HashMap<String, String> liability = new HashMap<>();
        liability.put("name", CAT_LIABILITIES);
        liability.put("id", AccountEntity.LIABILITY);
        accs.add(liability);
        return accs;
    }


    public List<Map<String, String>> getChartDataFields(String userId, String type) throws Exception {
        List<Map<String, String>> data = accountService.getLeafAccountList(userId, type);
        //Here I want to add the "Total" as an item
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "total");
        map.put("name", "Total");
        data.add(map);
        return data;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
//PRIVATE
    //Income & Expenses
    private List<Map<String, Object>> getDataSummation(String userId, String year, String type)
            throws Exception {
        List<Map<String, Object>> out = new ArrayList<>();
        //for all months
        List<AccountEntity> accs = accountRepo.findByUser_IdAndTypeOrderByText(userId, type);
        for (int month = 1; month <= 12; month++) {
            //get month transaction
            List<TransactionDTO> trans = transactionService.getYearMonthTransactions(userId, year, month); //for all accounts
            //for all accounts
            HashMap<String, Object> map = new HashMap<>();
            map.put("Month", getMonthName(month));
            double monthTotal = 0;
            for (AccountEntity a : accs) {
                double balance = transactionService.computeBalance(a.getId(), a.getType(), trans);
                double rate = getExchangeRate(a.getCurrency());
                balance *= rate;
                map.put(a.getText(), balance);
                monthTotal += balance;
            }
            map.put("Total", monthTotal);
            out.add(map);
        }
        return out;
    }

    private double getExchangeRate(String currency) {
        if (currency.equals(AccountEntity.USD)) {
            return usdRate;
        } else if (currency.equals(AccountEntity.SAR)) {
            return sarRate;
        }
        return 1; //EGP
    }

    //In Assets and Liabilities, we must compute balance from the very start up to the target month
    //So the looping should be based on complete Account history
    private List<Map<String, Object>> getTrendDataBalances(String userId, String year,
                                                           String type) throws Exception {
        List<Map<String, Object>> balanceData = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            HashMap<String, Object> month = new HashMap<>();
            month.put("Month", getMonthName(i + 1));
            month.put("Total", 0.0);
            balanceData.add(month);
        }
        //determine interval, smallest and largest date
        List<AccountEntity> accounts = accountRepo.findByUser_IdAndTypeOrderByText(userId, type);
        for (AccountEntity account : accounts) {
            //Get all transactions with computed balance
            List<TransactionDTO> transactions = transactionService.getTransactions(userId, account.getId());
            for (int month = 0; month <= 11; month++) {
                double monthBal = fetchMonthBalance(year, month + 1, transactions);
                monthBal *= getExchangeRate(account.getCurrency());
                Map<String, Object> map = balanceData.get(month);
                map.put(account.getText(), monthBal);
                Double total = (Double) map.get("Total");
                map.put("Total", total + monthBal);
            }
        }

        return balanceData;
    }

    //1 based date is sent
    private double fetchMonthBalance(String year, int month, List<TransactionDTO> ts) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = sdf.parse(year + "-" + String.valueOf(month + 1) + "-01"); //the first invalid date
        double balance = 0;
        for (TransactionDTO t : ts) {
            Date transDate = sdf.parse(t.getDate());
            if (transDate.before(myDate)) {
                balance = t.getBalance();
            } else {
                break;
            }
        }
        return balance;
    }

    private List<Map<String, Object>> getTrendDataTotals(String userId, String year) throws Exception {
        // Add Months Entries
        List<Map<String, Object>> out = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            HashMap<String, Object> month = new HashMap<>();
            month.put("Month", getMonthName(i + 1));
            month.put(CAT_ASSETS, 0.0);        //initialization
            month.put(CAT_LIABILITIES, 0.0);    //initialization
            out.add(month);
        }

        //Expenses
        String type = AccountEntity.EXPENSE;
        String totalName = CAT_EXPENSES;
        getTotalSummation(userId, year, type, totalName, out);

        //Income
        type = AccountEntity.INCOME;
        totalName = CAT_INCOME;
        getTotalSummation(userId, year, type, totalName, out);

        //Asset Balances
        type = AccountEntity.ASSET;
        totalName = CAT_ASSETS;
        getTotalsWithBalance(userId, year, type, totalName, out);

        //Liabilities Balances
        type = AccountEntity.LIABILITY;
        totalName = CAT_LIABILITIES;
        getTotalsWithBalance(userId, year, type, totalName, out);
        return out;
    }

    private void getTotalSummation(String userId,
                                   String year, String type, String totalName,
                                   List<Map<String, Object>> out) throws Exception {
        List<AccountEntity> accs;
        accs = accountRepo.findByUser_IdAndTypeOrderByText(userId, type);
        for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
        	List<TransactionDTO> transactions = transactionService.getYearMonthTransactions(userId, year, monthIndex); //for all accounts
            double total = 0;
            for (AccountEntity account : accs) {
                double balance = transactionService.computeBalance(account.getId(), account.getType(), transactions);
                total += (balance * getExchangeRate(account.getCurrency()));
            }
            out.get(monthIndex - 1).put(totalName, total);
        }
    }

    private void getTotalsWithBalance(String userId,
                                      String year, String type, String totalName, List<Map<String, Object>> out) throws Exception {

        List<AccountEntity> accs = accountRepo.findByUser_IdAndTypeOrderByText(userId, type);
        for (AccountEntity account : accs) {
            //Get all transactions with computed balance
        	List<TransactionDTO> ts = transactionService.getTransactions(userId, account.getId()); //for all years & one account
            for (int month = 0; month <= 11; month++) {
                double balance = fetchMonthBalance(year, month + 1, ts);
                balance *= getExchangeRate(account.getCurrency());
                double old = (Double) out.get(month).get(totalName);
                out.get(month).put(totalName, old + balance);
            }
        }
    }

    private String getMonthName(int month) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        String monthName = months[month - 1];
        return monthName.substring(0, 3); //return first three letters
    }


    private Map<String, Map<String, Object>> getTotalsAllYearsAllAccountTypes(String userId) throws Exception {
        // Add All years Entries
//        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        Map<String, Map<String, Object>> out = new HashMap<>();
        List<String> years = transactionService.getYearList(userId);
        for (String yearString : years) {
            HashMap<String, Object> year = new HashMap<>();
//            month.put("Month", getMonthName(i + 1));
//            String yearString = year1;
            year.put("Month", yearString);
//            year.put("Year", years.get(i));
            year.put(CAT_ASSETS, 0.0);        //initialization
            year.put(CAT_LIABILITIES, 0.0);    //initialization
            out.put(yearString, year);
        }
//
//      //Expenses
        String accountType = AccountEntity.EXPENSE;
        String totalName = CAT_EXPENSES;
        getTotalsAllYears(userId, years, accountType, totalName, out);

//        //Income
        accountType = AccountEntity.INCOME;
        totalName = CAT_INCOME;
        getTotalsAllYears(userId, years, accountType, totalName, out);

//        //Asset Balances
        accountType = AccountEntity.ASSET;
        totalName = CAT_ASSETS;
        getTotalsWithBalance(userId, years, accountType, totalName, out);

//        //Liabilities Balances
        accountType = AccountEntity.LIABILITY;
        totalName = CAT_LIABILITIES;
        getTotalsWithBalance(userId, years, accountType, totalName, out);
        return out;
    }


    private void getTotalsAllYears(String userId,
                                   List<String> years, String accountType, String totalName,
                                   Map<String, Map<String, Object>> out) throws Exception {

        List<AccountEntity> expenseAccounts = accountRepo.findByUser_IdAndTypeOrderByText(userId, accountType);

        for (String year : years) {
            List<TransactionDTO> transactions = transactionService.getYearTransactions(userId, year); //for all expenseAccounts
            double total = 0;
            for (AccountEntity account : expenseAccounts) {
                double balance = transactionService.computeBalance(account.getId(), account.getType(), transactions);
                total += (balance * getExchangeRate(account.getCurrency()));
            }
            out.get(year).put(totalName, total);
        }
    }

    private void getTotalsWithBalance(String userId,
                                      List<String> years, String type, String totalName,
                                      Map<String, Map<String, Object>> out) throws Exception {

        List<AccountEntity> accounts = accountRepo.findByUser_IdAndTypeOrderByText(userId, type);
        for (AccountEntity account : accounts) {
            //Get all transactions with computed balance
        	List<TransactionDTO> transactions = transactionService.getTransactions(userId, account.getId()); //for all years & one account
            for (String year : years) {
                double balance = fetchYearBalance(year, transactions);
                balance *= getExchangeRate(account.getCurrency());
                double old = (Double) out.get(year).get(totalName);
                out.get(year).put(totalName, old + balance);
            }
        }
    }


    //1 based date is sent
    private double fetchYearBalance(String year, List<TransactionDTO> transactions) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yearPlus = String.valueOf(Integer.parseInt(year) + 1);
        Date myDate = sdf.parse(yearPlus + "-01" + "-01"); //the first invalid date
        double balance = 0;
        for (TransactionDTO transaction : transactions) {
            Date transDate = sdf.parse(transaction.getDate());
            if (transDate.before(myDate)) {
                balance = transaction.getBalance();
            } else {
                break;
            }
        }
        return balance;
    }

}

