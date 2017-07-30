package pf.bdd;

import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pf.account.Account;
import pf.account.AccountService;
import pf.charts.ChartService;
import pf.email.Mailer;
import pf.transaction.TransactionService;
import pf.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
/**
 * Created by ahmedhammad on 6/28/16.
 */

//@RunWith(SpringRunner.class)
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)

public class ChartStats extends BddBase {


	@Autowired
	TransactionService transactionService;
	@Autowired
	UserService userService;
	@Autowired
	AccountService accountService;
	@Autowired
	ChartService chartService;
	
	@Autowired
	Mailer mailer;
	
    String userId, userEmail;
    HashMap<String, String> accountMap = new HashMap<String, String>();
    List<Map<String, Object>> trendData;

    @BeforeStory
    public void beforeStory() throws Exception {
    }

    @AfterStory
    public void afterStory() throws Exception {
    	if(null != userId && userId.isEmpty() == false){
    		userService.deleteUser(userId);
    	}
    }

    @BeforeScenario
    public void beforeEachScenario() throws Exception {    	
    }

    @AfterScenario
    public void afterEachScenario() throws Exception {
    }

    @Given("user email '$email' and password '$secret' and USD rate is $usdRate and SAR rate is $sarRate")
    public void given1(String email, String password, double usdRate, double sarRate) throws Exception {
        userEmail = email;
        userId = userService.registerUser(email, password, password, mailer, accountService,
                usdRate, sarRate);
    }

    @Given("user have these accounts $table")
    public void given1(ExamplesTable table) throws Exception {
        for (Map<String, String> row : table.getRows()) {
            String name = row.get("name");
            String type = row.get("type");
            String currency = row.get("currency");
            String accountId = accountService.create(userId, name, "desc", getAccountType(type), currency);
            accountMap.put(name, accountId);
        }
    }

    String getAccountType(String type) {
        if ("asset".equals(type)) return Account.ASSET;
        if ("expense".equals(type)) return Account.EXPENSE;
        if ("income".equals(type)) return Account.INCOME;
        else return Account.LIABILITY;
    }

    @Given("have these transactions: $table")
    public void given3(ExamplesTable table) throws Exception {
        for (Map<String, String> row : table.getRows()) {
            String fromAccount = row.get("fromAccount");
            String toAccount = row.get("toAccount");
            String date = row.get("date");
            String desc = row.get("desc");
            String amount = row.get("amount");
            assertNotNull(fromAccount + "is null", accountMap.get(fromAccount));
            assertNotNull(toAccount + " is NULL?!", accountMap.get(toAccount));
            transactionService.saveTransaction(userId, date, desc,
                    accountMap.get(fromAccount), accountMap.get(toAccount), amount);
        }
    }

    @When("calculating trend data for category $category in year $year")
    public void when1(String category, String year) throws Exception {
//        ChartService chartMgmt = jdbcFactory.createChartMgmt();
        trendData = chartService.getTrendData(userId, userEmail, year, category);
    }

    @Then("balance of account '$account' in $month is $amount")
    public void then1(String account, String month, double amount) {
        int monthCode = getMonthCode(month);
        assertEquals(amount, (Double) trendData.get(monthCode).get(account), 0.0001);
    }

    int getMonthCode(String month) {
//        int jan = 0, feb = 1, mar = 2, apr = 3, may = 4, jun = 5, jul = 6, aug = 7, sep = 8, oct = 9, nov = 10, dec = 11;
        if (month.equals("jan")) return 0;
        if (month.equals("feb")) return 1;
        if (month.equals("mar")) return 2;
        if (month.equals("apr")) return 3;
        if (month.equals("may")) return 4;
        if (month.equals("jun")) return 5;
        if (month.equals("jul")) return 6;
        if (month.equals("aug")) return 7;
        if (month.equals("sep")) return 8;
        if (month.equals("oct")) return 9;
        if (month.equals("nov")) return 10;
        return 11;
    }


    @When("calculating trend data total statistics for year $year")
    public void when2(String year) throws Exception {
//        StoreFactory jdbcFactory = Settings.getStoreFactoryForJdbc();
//        ChartService chartMgmt = jdbcFactory.createChartMgmt();
        trendData = chartService.getTrendData(userId, userEmail, year, "totals");

    }


    @Then("Income balance of $month is $amount")
    public void then2(String month, double amount) {
        int monthCode = getMonthCode(month);
        assertEquals(amount, (Double) trendData.get(monthCode).get("Income"), 0.0001);
    }


    @Then("Expenses balance in $month is $amount")
    public void then3(String month, double amount) {
        int monthCode = getMonthCode(month);
        assertEquals(amount, (Double) trendData.get(monthCode).get("Expenses"), 0.0001);
    }


    @Then("Liabilities balance in $month is $amount")
    public void then4(String month, double amount) {
        int monthCode = getMonthCode(month);
        assertEquals(amount, (Double) trendData.get(monthCode).get("Liabilities"), 0.0001);
    }


    @Then("Assets balance in $month is $amount")
    public void then5(String month, double amount) {
        int monthCode = getMonthCode(month);
        assertEquals(amount, (Double) trendData.get(monthCode).get("Assets"), 0.0001);
    }


/////////////
    Map<String, Map<String, Object>> allYearsTrendData;

    @When("calculating trend data for all categories for all years")
    public void when3() throws Exception {
        allYearsTrendData = chartService.getTrendDataAllYears(userId, userEmail);
    }

    @Then("balance of expenses in year $year is $balance")
    public void then6(String year, double balance) {
        Map<String, Object> ob = allYearsTrendData.get(year);
        double returnedBalance = (Double)ob.get("Expenses");
        assertEquals(balance, returnedBalance, 0.0001);
    }

    @Then("balance of income in year $year is $balance")
    public void then7(String year, double balance) {
        Map<String, Object> ob = allYearsTrendData.get(year);
        double returnedBalance = (Double)ob.get(ChartService.CAT_INCOME);
        assertEquals(balance, returnedBalance, 0.0001);
    }

    @Then("balance of asset in year $year is $balance")
    public void then8(String year, double balance) {
        Map<String, Object> ob = allYearsTrendData.get(year);
        double returnedBalance = (Double)ob.get(ChartService.CAT_ASSETS);
        assertEquals(balance, returnedBalance, 0.0001);
    }

    @Then("balance of liability in year $year is $balance")
    public void then9(String year, double balance) {
        Map<String, Object> ob = allYearsTrendData.get(year);
        double returnedBalance = (Double)ob.get(ChartService.CAT_LIABILITIES);
        assertEquals(balance, returnedBalance, 0.0001);
    }



}
