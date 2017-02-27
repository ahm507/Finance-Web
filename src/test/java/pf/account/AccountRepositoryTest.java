package pf.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pf.user.UserEntity;
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

    @Test
    public void findByUserId() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        List<AccountEntity> accounts = accountRepo.findByUserOrderByText(user);
        assertEquals(18, accounts.size());
    }
    
    
  @Test
    public void findByUserAndParent() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
      List<AccountEntity> accounts = accountRepo.findByUserAndParentOrderByText(user, AccountEntity.ROOT_ACCOUNT_ID);
      final int totalAccountsUnderRoot = 4;
      assertEquals(totalAccountsUnderRoot, accounts.size());
    }

    @Test
    public void findByUserAndId() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        AccountEntity account = accountRepo.findByUserAndId(user, accountId);
        assertNotNull(account);
    }

    @Test
    public void findByUserAndTextAndParent() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<AccountEntity> accounts = accountRepo.findByUserAndTextAndParentOrderByText(user, "Gifts", parentID);
        assertEquals(1, accounts.size());
    }

    @Test
    public void findByUserAndType() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        List<AccountEntity> accounts = accountRepo.findByUserAndTypeOrderByText(user, AccountEntity.INCOME);
        assertEquals(7, accounts.size());
    }

    @Test
    public void findByUserAndParentAndText() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<AccountEntity> accounts = accountRepo.findByUserAndParentAndTextOrderByText(user, AccountEntity.ROOT_ACCOUNT_ID, "income");
        assertEquals(1, accounts.size());
    }
    
    @Test
    public void findByUserAndParentAndType() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<AccountEntity> accounts = accountRepo.findByUserAndParentAndTypeOrderByText(user, parentID , AccountEntity.EXPENSE);
        assertEquals(5, accounts.size());
    }

    
    @Test
    public void findByUser_IdAndTypeAndParentNot() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<AccountEntity> accounts = accountRepo.findByUser_IdAndTypeAndParentNotOrderByText(user.getId(), AccountEntity.EXPENSE, AccountEntity.ROOT_ACCOUNT_ID);
        assertEquals(5, accounts.size());
    }

    @Test
    public void findByUser_IdAndId() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        AccountEntity account = accountRepo.findByUser_IdAndId(user.getId(), accountId);
        assertNotNull(account);
    }

    
    @Test
    public void findByUser_IdAndType() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
//        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentID = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        List<AccountEntity> accounts = accountRepo.findByUser_IdAndTypeOrderByText(user.getId(), AccountEntity.ASSET);
        assertEquals(3, accounts.size());
    }
    
    
    @Test
    public void findByUser_IdAndIdAndParent() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
        String parentId = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        AccountEntity account = accountRepo.findByUser_IdAndIdAndParent(user.getId(), accountId, parentId);
        assertNotNull(account);
    }
    
    
    
    @Test
    public void findByUser_EmailAndId() {
        UserEntity user = userRepo.findByEmail("test@test.test");
        assertNotNull(user);
        String accountId = "46818ca8-690a-413f-8e0d-6c88b8835971";
//        String parentId = "c8cfc2b1-6239-4940-b3bc-98d3e0108519"; //from db file
        AccountEntity account = accountRepo.findByUser_EmailAndId(user.getEmail(), accountId);
        assertNotNull(account);
    }
    
    
    
    
}
