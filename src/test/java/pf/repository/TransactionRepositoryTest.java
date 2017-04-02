package pf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pf.account.Account;
import pf.account.AccountRepository;
import pf.transaction.Transaction;
import pf.transaction.TransactionRepository;
import pf.user.User;
import pf.user.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



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
		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);
		List<Transaction> transactions = transRepo.findByUserOrderByDate(user);
		assertEquals(17, transactions.size());
		// assertThat(transactions.size(), hasSize(13));
	}

	@Test
	public void findByUserAndwithdrawAccountOrDepositAccount() {
		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);
		String accountId = "2100ba44-4d4d-49dd-a358-8ceb43ff2714";// Liability
		Account account = accountRepo.findByUserAndId(user, accountId);
		List<Transaction> transactions = transRepo.queryByUserAndAccountOrderByDate(user, account);
		assertEquals(2, transactions.size());
	}

	@Test
	public void findByUserAndDateBetween() {
		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		List<Transaction> transactions = transRepo.findByUserAndDateBetweenOrderByDate(user, "2016/08/19", "2016/09/19");
		assertEquals(4, transactions.size());
		assertEquals(1, transRepo.findByUserAndDateBetweenOrderByDate(user, "2016/10/19", "2016/10/19").size());
		assertEquals(1, transRepo.findByUser_IdAndDateBetweenOrderByDate(user.getId(), "2016/10/19", "2016/10/19").size());
		  
	}

	@Test
	public void byId() {
		Transaction trans = transRepo.queryById("78db8700-879f-4dfb-9fc6-b1dc054b0c8e");
		assertNotNull(trans);
	}

	@Test
	public void findMinAndMaxDate() {
		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);
		String minMax = transRepo.queryMinAndMaxDate(user);
		assertEquals("2016-02-19,2017-03-19", minMax); // concatenated by ,
	}

	@Test
	public void findByUserAndDateBetweenAndWithdrawAccountOrDepositAccount() {

		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		Account assetAccount = accountRepo.findById("52e2a234-498a-44da-abc2-5c95807ba531");// asset
																									// account
		assertNotNull(assetAccount);

		List<Transaction> transactions = transRepo.queryByUserAndDateBetweenAndAccountOrderByDate(
				user, "2016/08/19", "2016/09/19", assetAccount);
		assertEquals(4, transactions.size());

	}

	@Test
	public void queryByUserAndAccount() {

		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		Account assetAccount = accountRepo.findById("52e2a234-498a-44da-abc2-5c95807ba531");// asset																								// account
		assertNotNull(assetAccount);

		List<Transaction> transactions = transRepo.queryByUserAndAccountOrderByDate(user, assetAccount);
		assertEquals(14, transactions.size());

	}

	@Test
	@Transactional
	public void deleteByUser_Id() {

		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		List<Transaction> transactions = transRepo.findByUser_Id(user.getId()); 
		assertEquals(17, transactions.size());
		
//		transRepo.deleteByUser_Id(user.getId());
		transRepo.deleteByUser(user);
		
		List<Transaction> transactions2 = transRepo.findByUser_Id(user.getId());
		assertEquals(0, transactions2.size());
		
		//Insert them Again
		transRepo.save(transactions);

		transactions2 = transRepo.findByUser_Id(user.getId());
		assertEquals(17, transactions.size());

	}

	@Test
	public void insertTransaction() {

		User user = userRepo.findByEmail("test@test.test");
		assertNotNull(user);

		Account assetAccount = accountRepo.findById("52e2a234-498a-44da-abc2-5c95807ba531");// asset																								// account
		assertNotNull(assetAccount);
		String uuid = UUID.randomUUID().toString();
		String depositId = "2100ba44-4d4d-49dd-a358-8ceb43ff2714";
		String withdrawId = "4018d883-5936-468b-8fd8-d0f84adad927";
		String description = "test description";
		String date = "2017-02-02 19:30:22";
		Transaction transEntity = new Transaction(uuid,
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
