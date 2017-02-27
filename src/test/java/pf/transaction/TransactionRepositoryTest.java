package pf.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pf.account.AccountEntity;
import pf.account.AccountRepository;
import pf.user.UserEntity;
import pf.user.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = PfApplication.class)
//@WebAppConfiguration

@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql({"/test-data.sql"})
public class TransactionRepositoryTest {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private TransactionRepository transRepo;

	@Test
	public void findByUserEmail() {
		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);
		List<TransactionEntity> transactions = transRepo.findByUserOrderByDate(user);
		assertEquals(13, transactions.size());
		// assertThat(transactions.size(), hasSize(13));
	}

	@Test
	public void findByUserAndwithdrawAccountOrDepositAccount() {
		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);
		String accountId = "2100ba44-4d4d-49dd-a358-8ceb43ff2714";// Liability
		AccountEntity account = accountRepo.findByUserAndId(user, accountId);
		List<TransactionEntity> transactions = transRepo.queryByUserAndAccountOrderByDate(user, account);
		assertEquals(1, transactions.size());
	}

	@Test
	public void findByUserAndDateBetween() {
		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		List<TransactionEntity> transactions = transRepo.findByUserAndDateBetweenOrderByDate(user, "2016/08/19", "2016/09/19");
		assertEquals(4, transactions.size());
		assertEquals(1, transRepo.findByUserAndDateBetweenOrderByDate(user, "2016/10/19", "2016/10/19").size());
		assertEquals(1, transRepo.findByUser_IdAndDateBetweenOrderByDate(user.getId(), "2016/10/19", "2016/10/19").size());
		  
	}

	@Test
	public void byId() {
		TransactionEntity trans = transRepo.queryById("78db8700-879f-4dfb-9fc6-b1dc054b0c8e");
		assertNotNull(trans);
	}

	@Test
	public void findMinAndMaxDate() {
		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);
		String minMax = transRepo.queryMinAndMaxDate(user);
		assertEquals("2016-02-19,2016-12-19", minMax); // concatenated by ,
	}

	@Test
	public void findByUserAndDateBetweenAndWithdrawAccountOrDepositAccount() {

		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		AccountEntity assetAccount = accountRepo.findById("52e2a234-498a-44da-abc2-5c95807ba531");// asset
																									// account
		assertNotNull(assetAccount);

		List<TransactionEntity> transactions = transRepo.queryByUserAndDateBetweenAndAccountOrderByDate(
				user, "2016/08/19", "2016/09/19", assetAccount);
		assertEquals(4, transactions.size());

	}

	@Test
	public void queryByUserAndAccount() {

		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		AccountEntity assetAccount = accountRepo.findById("52e2a234-498a-44da-abc2-5c95807ba531");// asset																								// account
		assertNotNull(assetAccount);

		List<TransactionEntity> transactions = transRepo.queryByUserAndAccountOrderByDate(user, assetAccount);
		assertEquals(11, transactions.size());

	}

	@Test
	@Transactional
	public void deleteByUser_Id() {

		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		List<TransactionEntity> transactions = transRepo.findByUser_Id(user.getId()); 
		assertEquals(13, transactions.size());
		
//		transRepo.deleteByUser_Id(user.getId());
		transRepo.deleteByUser(user);
		
		List<TransactionEntity> transactions2 = transRepo.findByUser_Id(user.getId());
		assertEquals(0, transactions2.size());
		
		//Insert them Again
		transRepo.save(transactions);

		transactions2 = transRepo.findByUser_Id(user.getId());
		assertEquals(13, transactions.size());

	}

	@Test
	public void insertTransaction() {

		UserEntity user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		AccountEntity assetAccount = accountRepo.findById("52e2a234-498a-44da-abc2-5c95807ba531");// asset																								// account
		assertNotNull(assetAccount);
		String uuid = UUID.randomUUID().toString();
		String depositId = "2100ba44-4d4d-49dd-a358-8ceb43ff2714";
		String withdrawId = "4018d883-5936-468b-8fd8-d0f84adad927";
		String description = "test description";
		String date = "2017-02-02 19:30:22";
		TransactionEntity transEntity = new TransactionEntity(uuid,
				user,
				date, description,
				accountRepo.findByUser_IdAndId(user.getId(), withdrawId),
				accountRepo.findByUser_IdAndId(user.getId(), depositId),
				Double.parseDouble("1234567.89"));
		transRepo.save(transEntity);

		assertEquals(1234567.89, transEntity.getAmount(), 0.0001);
		transRepo.delete(transEntity);

	}








}
