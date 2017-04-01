package pf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pf.account.AccountService;
import pf.email.Mailer;
import pf.user.InvalidEmailSyntaxException;
import pf.user.UserEntity;
import pf.user.UserRepository;
import pf.user.UserService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = PfApplication.class)
//@WebAppConfiguration
//@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@InjectMocks 
	private UserService userService;

	@Mock 
	private UserRepository mockUserRepo;

    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule(); //create the mocks based on the @Mock annotation 


	@Before
    public void setupMock() {
	       //MockitoAnnotations.initMocks(this);
//	       userService = new UserService ();
    }	
	
	//mvn -Dtest=TestCircle#xyz test
	//mvn -Dtest=pf.service.UserServiceTest#login test
	//mvn -Dtest=pf.repository.AccountRepositoryTest#findByUserId test
	
	@Test
//	@Timed(millis=1000)
//	@Repeat(10)
	public void login() throws Exception {
        String email = "test@test.com";
        String password = "secret";
        UserEntity testUser = new UserEntity("123", email, password);
        when(mockUserRepo.findByEmailAndPassword(email, UserService.md5(password))).thenReturn(testUser);
        UserEntity user = userService.login(email, password);
        assertEquals("123", user.getId());
	}
	
	@Test(expected = InvalidEmailSyntaxException.class)
	public void loginInvalidEmail() throws InvalidEmailSyntaxException, Exception {
//        String email = "test@test.com";
        String password = "secret";
//        UserEntity testUser = new UserEntity("123", email, password);
//        when(mockUserRepo.findByEmailAndPassword(email, UserService.md5(password))).thenReturn(testUser);
        userService.login("invalid email syntax", password);
	}
	
	@Test(expected = Exception.class)
	public void loginInvalidPassword() throws Exception {
        String email = "test@test.com";
//        String password = "secret";
//        UserEntity testUser = new UserEntity("123", email, password);
//        when(mockUserRepo.findByEmailAndPassword(email, UserService.md5(password))).thenReturn(testUser);
        assertEquals("123", userService.login(email, "wrong password"));
	}
	
	@Test(expected = Exception.class)
	public void loginNullPassword() throws Exception {
        String email = "test@test.com";
//        String password = "secret";
//        UserEntity testUser = new UserEntity("123", email, password);
//        when(mockUserRepo.findByEmailAndPassword(email, UserService.md5(password))).thenReturn(testUser);
        assertEquals("123", userService.login(email, null));
	}
	
	
//	registerUser(String email, String password, String password2, Mailer mailer,
//			   Account account, double usdRate, double sarRate)
	
	@Mock
	AccountService mockAccount;
	
	@Mock
	Mailer mockMailer;
	
	
	@Test//(expected = Exception.class)
	public void registerInvalidEmail() throws Exception {
//        String email = "test@test.com";
//        String password1 = "secret";
//        String password2 = "secret";
        
//      UserEntity testUser = new UserEntity("123", email, password1);
//		UserEntity userRow = userRepo.findByEmail(email);
//		userRepo.save(newUser);
//		mailer.sendVerifyEmail(email, newUser.getVerification_key());
//		account.create(userId, "Salary", "", AccountStore.INCOME, AccountStore.EGP);

//        when(mockUserRepo.findByEmail(anyString())).thenReturn(testUser);
//        when(mockUserRepo.save(testUser)).thenReturn(testUser);
//        when(mockMailer.sendVerifyEmail(anyString(), anyString())).thenReturn(true); 
//        when(mockAccount.create(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn("uuuid of created object");        
//        
        String uuid = userService.registerUser("test@test.com", "secret", "secret", mockMailer, mockAccount, 1, 1);
        assertNotNull(uuid);
        assertNotEquals(uuid, ""); //not empty
//        assertThat(uuid, is(not(null)).and(is(not(empty()))));
        
	}
	
}
