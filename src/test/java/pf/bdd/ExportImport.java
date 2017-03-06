package pf.bdd;

import au.com.bytecode.opencsv.CSVReader;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pf.account.AccountEntity;
import pf.account.AccountService;
import pf.service.BackupService;
import pf.service.RestoreService;
import pf.transaction.TransactionEntity;
import pf.transaction.TransactionRepository;
import pf.transaction.TransactionService;
import pf.user.UserEntity;
import pf.user.UserRepository;
import pf.user.UserService;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ExportImport extends BddBase {


	@Autowired
	TransactionService transactionService;
	@Autowired
	TransactionRepository transactionRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	UserService userService;
	@Autowired
	AccountService accountService;
//	@Autowired
//	ChartService chartService;
	@Autowired
	BackupService backup;
	@Autowired
	RestoreService restore;
	
	
//    AccountService accountService;
//    UserService userService;
//    Transaction transactionService;
    String userId, userEmail;
    HashMap<String, String> accountMap = new HashMap<String, String>();
    double m_balance = 0;
    ExamplesTable accountsTable;
    ExamplesTable transactionsTable;
    String exportContents;

    //    AfterScenario&BeforeScenario
    @BeforeStory
	@Transactional    
    public void beforeStory() throws Exception {
    	if(null != userId && userId.isEmpty() == false){
    		userService.deleteUser(userId);
    	}
    }

    @AfterStory
    public void afterStory() throws Exception {
    	if(null != userId && userId.isEmpty() == false){
    		userService.deleteUser(userId);
    	}
    }

    @Given("user email '$email' and password '$password'")
    public void given(String email, String password) throws Exception {
        userEmail = email;
        
        UserEntity user = userRepo.findByEmail(email);
        if(user != null) {
        	userService.deleteUser(userId);
        }
        
        userId = userService.registerUser(email, password, password, new DummyMailer(), accountService, 1.0, 1.0);
    }

    @Given("have these accounts: $table")
    public void given2(ExamplesTable table) throws Exception {
        accountsTable = table;
        for (Map<String, String> row : table.getRows()) {
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
        transactionsTable = table;
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

    @When("exporting and then importing it")
    public void when1() throws Exception {
//        BackupService backup = Settings.getStoreFactoryForJdbc().createBackup();
        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        backup.backup(userId, writer);
        exportContents = buffer.toString();

        //Restoring the contents
//        RestoreService restore = Settings.getStoreFactoryForJdbc().createRestore();
        StringReader stringReader = new StringReader(exportContents);
        CSVReader reader = new CSVReader(stringReader);
        restore.importFile(reader, userId);
    }

    @Then("ensure accounts are:$table")
    public void then1(ExamplesTable table) throws Exception {
        List<AccountEntity> accounts = accountService.getAccountsTree(userEmail);
//        assertEquals("Account count is not matched", accountsTable.getRowCount(), table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            Map<String, String> row = table.getRow(i);
            String name = row.get("name");
            String type = row.get("type");
            String currency = row.get("currency");
//            Map<String, String> row2 = findRow("name", name, accountsTable);
            AccountEntity account = findAccount(accounts, name);
            assertNotNull("Account not found:" + name, account);
            assertEquals("Mismatched accounts", name, account.getText());
            assertEquals("Mismatched accounts", type, account.getType());
            assertEquals("Mismatched accounts", currency, account.getCurrency());
        }
    }

    AccountEntity findAccount(List<AccountEntity> accounts, String name) {
        for (AccountEntity account : accounts) {
            if (account.getText().equals(name)) {
                return account;
            }
            List<AccountEntity> kids = account.getChildren();
//            AccountService[] kidsArray = kids.toArray(new AccountService[kids.size()]);
            AccountEntity kid = findAccount(kids, name);
            if (kid != null) {
                return kid;
            }
        }
        return null;
    }


    @Then("ensure transactions are:$table")
    public void then2(ExamplesTable table) throws Exception {
//        assertEquals("transactions count is not matched", transactionsTable.getRowCount(), table.getRowCount());
//        TransactionStoreJdbc transactionDao = new TransactionStoreJdbc();
        List<TransactionEntity> transes = transactionRepo.findByUser_Id(userId);

        for (int i = 0; i < table.getRowCount(); i++) {
            Map<String, String> row = table.getRow(i);
            String fromAccount = row.get("fromAccount");
            String toAccount = row.get("toAccount");
            String date = row.get("date");
            String desc = row.get("desc");
            String amount = row.get("amount");
            TransactionEntity t = findTransaction(desc, transes);
            assertNotNull("Transaction Not found", t);
            AccountEntity withdrawAccount = accountService.getAccount(userId, t.getWithdrawId());
            assertEquals("Mismatched accounts", fromAccount, withdrawAccount.getText());
            AccountEntity depositAccount = accountService.getAccount(userId, t.getDepositId());
            assertEquals("Mismatched accounts", toAccount, depositAccount.getText());
            assertEquals("Mismatched accounts", date, t.getDate());
            assertEquals("Mismatched accounts", Double.parseDouble(amount), t.getAmount(), 0.0001);

        }
    }

    TransactionEntity findTransaction(String desc, List<TransactionEntity> transes) {

        for (TransactionEntity t : transes) {
            if (t.getDescription().equals(desc)) {
                return t;
            }
        }
        return null;
    }


}

