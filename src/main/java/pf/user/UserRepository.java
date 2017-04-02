package pf.user;


import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, Long> {

	User findByEmail(String email);

	User findById(String id);

	User findByEmailAndPassword(String email, String hashedPassword);

	User findByEmailAndResetPasswordKey(String email, String resetCode);

	User findByEmailAndVerificationKey(String email, String verificationCode);

	void deleteById(String userId);

}