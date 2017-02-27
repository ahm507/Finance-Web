package pf.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pf.account.AccountEntity;
import pf.account.AccountRepository;
import pf.account.AccountService;
import pf.email.Mailer;
import pf.transaction.TransactionRepository;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Integrates database, business logic and email system
 */

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	
	private final static Logger logger = Logger.getLogger(UserService.class.getName());

	public UserService() { //prevent instantiation
	}

	static public String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static String md5(String input) throws NoSuchAlgorithmException {
		String salt = "Some$SaltString#WithSpecialChars12@$@4&#%^$*-";
		String md5;
		MessageDigest digest = MessageDigest.getInstance("MD5");
		String inputSalted = input + salt;
		digest.update(inputSalted.getBytes(), 0, inputSalted.length());
		// Converts message digest value in base 16 (hex)
		md5 = new BigInteger(1, digest.digest()).toString(16);
		return md5;
	}

	public String registerUser(String email, String password, String password2, Mailer mailer,
	                           AccountService account, double usdRate, double sarRate)
			throws Exception {
		//check if user exist
		UserEntity userRow = userRepo.findByEmail(email);
		if(userRow != null) {
			logger.warning("This email is already used:" + email);
			throw new Exception("Email Already Used");
		}
		//create user business logic
		UserEntity newUser = createUserEntity(email, password, password2);
		//persist user
		int verified = 0;
		newUser.setVerified(verified);
		newUser.setUsd_rate(usdRate);
		newUser.setSar_rate(sarRate);
//		userRepo.insert(user2.getId(), user2.getEmail(), user2.getPassword(), user2.getDate(), verified,
//				user2.getVerification_key(), usdRate, sarRate);
		userRepo.save(newUser);
		//send email notification
		mailer.sendVerifyEmail(email, newUser.getVerification_key());
		createNewUserAccounts(newUser.getId(), account);
		return newUser.getId();
	}

	public String login(String email, String password)
			throws InvalidEmailSyntaxException, NoSuchAlgorithmException, InvalidPasswordException, UserNotExistException {
		if ( ! validEmailSyntax(email))
			throw new InvalidEmailSyntaxException();
		if (password == null || password.isEmpty())
			throw new InvalidPasswordException("InvalidPassword");

		UserEntity userRow = userRepo.findByEmailAndPassword(email, md5(password));
		if( userRow == null) {
			throw new UserNotExistException("UserNotExist"); // or may be not verified
		} else {
			return userRow.getId();
		}
	}

	public void resendVerifyEmail(String email) throws Exception {
		UserEntity user = userRepo.findByEmail(email);
		if(user != null) {
			Mailer mailer = new Mailer();
			mailer.sendVerifyEmail(email, user.getVerification_key());
		} else {
			throw new Exception("InvalidEmail");
		}
	}

	public void updateSettings(String email, String oldPass, String newPass1,
							   String newPass2, double usdRate, double sarRate) throws Exception {

		if (!oldPass.isEmpty()) { //Needs to change password also
			if(newPass1.isEmpty() || newPass1.equals(newPass2) == false) {
				throw new Exception("EmptyPassword|InvalidUSDRate|InvalidSARRate");
			}
			UserEntity userRow = userRepo.findByEmailAndPassword(email, md5(oldPass));
			if (userRow != null) { //Old password is OK
				userRow.setPassword(md5(newPass1));
				userRow.setUsd_rate(usdRate);
				userRow.setSar_rate(sarRate);
				userRepo.save(userRow);
//				userRepo.updateSettings(email, md5(newPass1), usdRate, sarRate);
			}

		} else {//No need to change password
				if (usdRate <= 0 || sarRate <= 0) {
					throw new Exception("InvalidUSDRate:Negative of zero rate is not allowed");
				}
				UserEntity user = userRepo.findByEmail(email);
				user.setUsd_rate(usdRate);
				user.setSar_rate(sarRate);
				userRepo.save(user);
			}
//
//
//		if (oldPass.isEmpty() == false || usdRate <= 0 || sarRate <= 0 ||
//				newPass1.isEmpty() || newPass2.isEmpty() ||
//				newPass1.equals(newPass2) == false) { // nothing valid to update
//			throw new Exception("EmptyPassword|InvalidUSDRate|InvalidSARRate");
//		} else if (oldPass.isEmpty()) {
//			UserEntity user = userRepo.findByEmail(email);
//			user.setUsd_rate(usdRate);
//			user.setSar_rate(sarRate);
//			userRepo.save(user);
////			userRepo.updateSettings(email, "", usdRate, sarRate);
//		} else {
//			UserEntity userRow = userRepo.findByEmailAndPassword(email, md5(oldPass));
////			UserStore userRow = userStore.findVerified(email, md5(oldPass));
//			if (userRow != null) { //Old password is OK
//				userRow.setPassword(md5(newPass1));
//				userRow.setUsd_rate(usdRate);
//				userRow.setSar_rate(sarRate);
//				userRepo.save(userRow);
////				userRepo.updateSettings(email, md5(newPass1), usdRate, sarRate);
//			} else {
//				throw new Exception("InvalidPasswordException");
//			}
//		}
	}

	public void resetPassword(String email, String code, String newPass1, String newPass2) throws Exception {
		UserEntity user = userRepo.findByEmailAndResetPasswordKey(email, code);
//		UserStore user = userStore.findWithResetCode(email, code);
		if(user != null) { //reset_password_key is OK
			if(newPass1.equals(newPass2) && newPass1.isEmpty() == false) {
				user.setPassword(newPass1);
				userRepo.save(user);
//				userStore.updateSettings(email, newPass1, 0, 0); // 0 is a special usdRate to ignore updating it
			} else {
				throw new Exception("MismatchedPasswordException");
			}
		} else {
			throw new Exception("InvalidResetPasswordKey");
		}
	}

	public boolean isExist(String email, String hashedPassword) throws Exception {
//		UserStore user = userStore.findVerified(email, password);
		UserEntity user = userRepo.findByEmailAndPassword(email, hashedPassword);
		return (user != null);
	}

	public UserEntity createUserEntity(String email, String password, String password2) throws Exception {

		if (!validEmailSyntax(email)) {
			throw new Exception("InvalidEmail");
		}
		if (password == null || password.isEmpty()) {
			throw new Exception("InvalidPasswordException");
		}
		if (!password.equals(password2)) {
			throw new Exception("MismatchedPasswordException");
		}

		UserEntity newUser = new UserEntity(getUUID(),  email, md5(password));
		newUser.setVerification_key(getUUID()); //Why?!
		return newUser;
	}

	public void sendResetEmail(String email) throws Exception {
		String resetCode = getUUID();
		UserEntity user = userRepo.findByEmail(email);
		if(user != null) { //means it is not verified yet
			user.setReset_password_key(resetCode);
			userRepo.save(user);
			Mailer mailer = new Mailer();
			mailer.sendResetEmail(email, resetCode);
		} else {
			throw new Exception("Email not found");
		}
	
	}
	
	public UserEntity verifyEmail(String email, String verificationCode) throws Exception {
//		UserStore user = userStore.findWithVerificationCode(email, code);
		UserEntity user = userRepo.findByEmailAndVerificationKey(email, verificationCode);
		if(user != null) { //means it is not verified yet
			user.setVerified(1);
			userRepo.save(user);
//			userStore.updateVerifiedFlag(email);
		} else {
			throw new Exception("User not found");
		}
		return user;
	}	
	
	public void sendFeedbackEmail(String email, String name, String title, String comments) throws Exception {
		
		if ( ! validEmailSyntax(email)) {
			throw new Exception("InvalidEmail");
		}
		if (comments == null || comments.isEmpty()) { 
			throw new Exception("EmptyFeedback");
		}
		Mailer mailer = new Mailer();
		mailer.sendFeedbackEmail(email, name, title, comments);		
	}

	private boolean validEmailSyntax(String email) {
		if (email == null || email.length() == 0 || email.indexOf('@') == -1 || email.indexOf('.') == -1) {
			return false;
		}

		if (email.length() < 5) {// minimum a@b.c
			return false;
		}

		return !(email.indexOf('\'') != -1 || email.indexOf('\"') != -1
				|| email.indexOf('\\') != -1 || email.indexOf('/') != -1
				|| email.indexOf(' ') != -1 || email.indexOf(',') != -1
				|| email.indexOf(';') != -1 || email.indexOf(',') != -1);

	}

	private void createNewUserAccounts(String userId, AccountService account) throws Exception {
		//Income Accounts
		account.createWithParent(userId, "Income", "", AccountEntity.INCOME, "0", AccountEntity.EGP);
		account.create(userId, "Salary", "", AccountEntity.INCOME, AccountEntity.EGP);
		account.create(userId, "Bonus", "", AccountEntity.INCOME, AccountEntity.EGP);
		account.create(userId, "Bank Profits", "", AccountEntity.INCOME, AccountEntity.EGP);
		account.create(userId, "General Income", "", AccountEntity.INCOME, AccountEntity.EGP);
		//Asset Accounts
		account.createWithParent(userId, "Assets", "", AccountEntity.ASSET, "0", AccountEntity.EGP);
		account.create(userId, "Cash in Wallet", "", AccountEntity.ASSET, AccountEntity.EGP);
		account.create(userId, "Current Bank Account", "", AccountEntity.ASSET, AccountEntity.EGP);
		account.create(userId, "Savings Bank Account", "", AccountEntity.ASSET, AccountEntity.EGP);
		account.create(userId, "General Asset", "", AccountEntity.ASSET, AccountEntity.EGP);
		//Liability accounts
		account.createWithParent(userId, "Liability", "", AccountEntity.LIABILITY, "0", AccountEntity.EGP);
		account.create(userId, "Master Card", "", AccountEntity.LIABILITY, AccountEntity.EGP);
		account.create(userId, "Visa Card", "", AccountEntity.LIABILITY, AccountEntity.EGP);
		account.create(userId, "General Liability", "", AccountEntity.LIABILITY, AccountEntity.EGP);
		//Expense Accounts
		account.createWithParent(userId, "Expenses", "", AccountEntity.EXPENSE, "0", AccountEntity.EGP);
		account.create(userId, "Food", "", AccountEntity.EXPENSE, AccountEntity.EGP);
		account.create(userId, "Clothes", "", AccountEntity.EXPENSE, AccountEntity.EGP);
		account.create(userId, "Education", "", AccountEntity.EXPENSE, AccountEntity.EGP);
		account.create(userId, "Car", "", AccountEntity.EXPENSE, AccountEntity.EGP);
		account.create(userId, "Charity", "", AccountEntity.EXPENSE, AccountEntity.EGP);
		account.create(userId, "General Expense", "", AccountEntity.EXPENSE, AccountEntity.EGP);
		//Other
		account.createWithParent(userId, "Other", "", AccountEntity.OTHER, "0", AccountEntity.EGP);
		account.create(userId, "Other", "", AccountEntity.OTHER, AccountEntity.EGP);
	}

	@Transactional
	public void deleteUser(String userId) {
		transactionRepo.deleteByUser_Id(userId);
		accountRepo.deleteByUser_Id(userId);
		userRepo.deleteById(userId);
	}

}
