package pf.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pf.user.UserEntity;
import pf.user.UserRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql({"/test-data.sql"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    public void findByEmail() {
        String email = "test@test.test";
        UserEntity user = userRepo.findByEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void findByEmailAndHashedPassword() {
        String email = "test@test.test";
        String hashedPassword = "caed27881edcf87a75fbaa57aaf4144c";
        UserEntity user = userRepo.findByEmailAndPassword(email, hashedPassword);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
     }

    @Test
    public void findByEmailAndResetCode() {
        String email = "test@test.test";
        String resetCode = "fake-reset-code";
        UserEntity user = userRepo.findByEmailAndResetPasswordKey(email, resetCode);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
     }

    @Test
    public void findByEmailAndVerificationCode() {
        String email = "test@test.test";
        String verificationCode = "6eb2fecc-2799-4106-a192-d0824108c2f9";
        UserEntity user = userRepo.findByEmailAndVerificationKey(email, verificationCode);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
     }

    @Test
    public void findById() {
        UserEntity user = userRepo.findById("4f680c93-838d-4329-9aba-7bedca232a89");
        assertNotNull(user);
    }

    @Test
    public void deleteAndSafe() {
        UserEntity user = userRepo.findById("4f680c93-838d-4329-9aba-7bedca232a89");
        assertNotNull(user);
        //Delete
        userRepo.delete(user);
        UserEntity userDeleted = userRepo.findById("4f680c93-838d-4329-9aba-7bedca232a89");
        assertNull(userDeleted);
        //insert
        userRepo.save(user);
        UserEntity userInserted = userRepo.findById("4f680c93-838d-4329-9aba-7bedca232a89");
        assertNotNull(userInserted);
    }




}
