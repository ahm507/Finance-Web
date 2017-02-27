package pf.bdd;

import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pf.BddBase;
import pf.account.AccountEntity;
import pf.account.AccountRepository;
import pf.account.AccountService;
import pf.transaction.TransactionService;
import pf.user.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;
//import de.codecentric.jbehave.junit.monitoring.SpringJUnitReportingRunner;  
 
//@RunWith(SpringRunner.class)
@SpringBootTest 
@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringJUnitReportingRunner.class)

public class AccountBalance extends BddBase {

//    AccountService accountMgmt;
//    UserService user;
    String userId, userEmail;
    HashMap<String, String> accountMap = new HashMap<String, String>();
//    Transaction transaction;
    
	@Autowired
	TransactionService transactionService;
	@Autowired
	UserService userService;
	@Autowired
	AccountService accountService;
    
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

    @Given("user email '$email' and password '$secret' and USD rate is $usdRate and SAR rate is $sarRate")
    public void given1(String email, String password, double usdRate, double sarRate) throws Exception {
        userEmail = email;
        userId = userService.registerUser(email, password, password, new DummyMailer(), accountService,
                usdRate, sarRate);
    }

    @Given("have these accounts: $table")
    public void given1(ExamplesTable table) throws Exception {
        for (Map<String, String> row : table.getRows()) {
            String name = row.get("name");
            String type = row.get("type");
            String currency = row.get("currency");
            String accountId = accountService.create(userId, name, "desc", getAccountType(type), currency);
            accountMap.put(name, accountId);
        }
    }

    protected String getAccountType(String type) {
	    if ("asset".equals(type)) return AccountEntity.ASSET;
	    if ("expense".equals(type)) return AccountEntity.EXPENSE;
	    if ("income".equals(type)) return AccountEntity.INCOME;
	    else return AccountEntity.LIABILITY;
	}
    
    
    @Given("have these transactions: $table")
    public void given3(ExamplesTable table) throws Exception {
        for (Map<String, String> row : table.getRows()) {
            String fromAccount = row.get("fromAccount");
            String toAccount = row.get("toAccount");
            String date = row.get("date");
            String desc = row.get("desc");
            String amount = row.get("amount");
//            transactionService = Settings.getStoreFactoryForJdbc().createTransactionMgmt();
            assertNotNull(fromAccount + "is null", accountMap.get(fromAccount));
            assertNotNull(toAccount + " is NULL?!", accountMap.get(toAccount));
            transactionService.saveTransaction(userId, date, desc,
                    accountMap.get(fromAccount), accountMap.get(toAccount), amount);
        }
    }

    List<AccountEntity> accounts;

    @When("getting account total balances")
    public void when1() throws Exception {
        accounts = accountService.getAccountsTree(userId, userEmail);//it computes balances
    }

    @Then("Income balance is $amount")
    public void then1(double amount) {
        AccountEntity income =  accounts.stream().filter(x -> "Income".equals(x.getText())).findAny().orElse(null);
        double balance = accountService.getBalanceEgp(income);
        assertEquals(amount, balance, 0.00001) ;
    }

	@Autowired
	AccountRepository accountRepo;

    
    @Then("Assets balance is $amount")
    public void then2(double amount) {        
        AccountEntity assets =  accounts.stream().filter(x -> "Assets".equals(x.getText())).findAny().orElse(null);
        double balance = accountService.getBalanceEgp(assets);
        assertEquals(amount, balance, 0.00001) ;
    }


}
