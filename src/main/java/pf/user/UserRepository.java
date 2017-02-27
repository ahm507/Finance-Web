package pf.user;


import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);

	UserEntity findById(String id);

	UserEntity findByEmailAndPassword(String email, String hashedPassword);

	UserEntity findByEmailAndResetPasswordKey(String email, String resetCode);

	UserEntity findByEmailAndVerificationKey(String email, String verificationCode);

	void deleteById(String userId);

}