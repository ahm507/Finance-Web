package pf.bdd;

import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pf.account.AccountService;
import pf.service.*;
import pf.account.AccountEntity;
import pf.transaction.TransactionDTO;
import pf.transaction.TransactionService;
import pf.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRange extends BddBase {

	@Autowired
    TransactionService transactionService;
	@Autowired
    UserService userService;
	@Autowired
    AccountService accountService;
	@Autowired
	ChartService chartService;
	    
    
    String userId;
    HashMap<String, String> accountMap = new HashMap<String, String>();
    double m_balance = 0;


    //AfterScenario&BeforeScenario
    //BeforeStory
    @AfterStory
    public void tearDown() throws Exception {
//        TestLib.cleanupTestDB();
    	if(null != null) {
    		userService.deleteUser(userId);
    	}
    }

    @Given("user email '$email' and password '$password'")
    public void given(String email, String password) throws Exception {
        //DB configurations
//        String rootPath = TestConfig.getTestRootPath();
//        Settings.setup(rootPath);
//        JdbcConnection.setup(Settings.getObject());
//        TestLib.cleanupTestDB();
//
//        accountService = Settings.getStoreFactoryForJdbc().createAccountMgmt();
//        userService = Settings.getStoreFactoryForJdbc().createUserMgmt();
        userId = userService.registerUser(email, password, password, new DummyMailer(), accountService, 1.0, 1.0);
    }

    @Given("have these accounts: $table")
    public void given2(ExamplesTable table) throws Exception {

//        HashMap<String, String> accountMap = new HashMap<>();
        for (Map<String, String> row : table.getRows()) {
//          name|type|
            String name = row.get("name");
            String type = row.get("type");
            String currency = row.get("currency");
            String accountId = accountService.create(userId, name, "desc", getAccountType(type), currency);
            accountMap.put(name, accountId);
        }

    }

    String getAccountType(String type) {
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
//            transaction = Settings.getStoreFactoryForJdbc().createTransactionMgmt();
            assertNotNull(accountMap.get(fromAccount));
            assertNotNull(accountMap.get(toAccount));
            transactionService.saveTransaction(userId, row.get("date"), row.get("desc"),
                    accountMap.get(fromAccount), accountMap.get(toAccount), row.get("amount"));
        }
    }

    @When("getting transactions of account '$accountMgmt' and year $year")
    public void when1(String account, String year) throws Exception {
        String accountId = accountMap.get(account);
        List<TransactionDTO> transes = transactionService.getYearTransactions(userId, accountId, year);
        m_balance = transes.get(transes.size() - 1).getBalance();
    }

    @Then("verify balance is $balance")
    public void then1(double balance) throws Exception {
        assertEquals(balance, m_balance, 0.000001);
    }



}

