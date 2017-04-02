package pf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pf.account.Account;
import pf.account.AccountRepository;
import pf.user.User;
import pf.user.UserRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest

//@Sql({"/test-data.sql"})
public class AccountRepositoryTest {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;

    //mvn -Dtest=pf.repository.AccountRepositoryTest#findByUserId test
    @Test
    public void findByUserId() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        List<Account> accounts = accountRepo.findByUserOrderByText(user);
        assertEquals(18, accounts.size());
    }
    
    
  @Test
    public void findByUserAndParent() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
      List<Account> accounts = accountRepo.findByUserAndParentOrderByText(user, Account.ROOT_ACCOUNT_ID);
      final int totalAccountsUnderRoot = 4;
      assertEquals(totalAccountsUnderRoot, accounts.size());
    }

    @Test
    public void findByUserAndId() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        Account account = accountRepo.findByUserAndId(user, accountId);
        assertNotNull(account);
    }

    @Test
    public void findByUserAndTextAndParent() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<Account> accounts = accountRepo.findByUserAndTextAndParentOrderByText(user, "Gifts", parentID);
        assertEquals(1, accounts.size());
    }

    @Test
    public void findByUserAndType() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        List<Account> accounts = accountRepo.findByUserAndTypeOrderByText(user, Account.INCOME);
        assertEquals(7, accounts.size());
    }

    @Test
    public void findByUserAndParentAndText() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<Account> accounts = accountRepo.findByUserAndParentAndTextOrderByText(user, Account.ROOT_ACCOUNT_ID, "income");
        assertEquals(1, accounts.size());
    }
    
    @Test
    public void findByUserAndParentAndType() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<Account> accounts = accountRepo.findByUserAndParentAndTypeOrderByText(user, parentID , Account.EXPENSE);
        assertEquals(5, accounts.size());
    }

    
    @Test
    public void findByUser_IdAndTypeAndParentNot() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<Account> accounts = accountRepo.findByUser_IdAndTypeAndParentNotOrderByText(user.getId(), Account.EXPENSE, Account.ROOT_ACCOUNT_ID);
        assertEquals(5, accounts.size());
    }

    @Test
    public void findByUser_IdAndId() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        Account account = accountRepo.findByUser_IdAndId(user.getId(), accountId);
        assertNotNull(account);
    }

    
    @Test
    public void findByUser_IdAndType() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<Account> accounts = accountRepo.findByUser_IdAndTypeOrderByText(user.getId(), Account.ASSET);
        assertEquals(3, accounts.size());
    }
    
    
    @Test
    public void findByUser_IdAndIdAndParent() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        String parentId = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        Account account = accountRepo.findByUser_IdAndIdAndParent(user.getId(), accountId, parentId);
        assertNotNull(account);
    }
    
    
    
    @Test
    public void findByUser_EmailAndId() {
        User user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentId = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        Account account = accountRepo.findByUser_EmailAndId(user.getEmail(), accountId);
        assertNotNull(account);
    }
    
    
    
    
}
