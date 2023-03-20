package jp.co.axa.apidemo.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.enumeration.UserRole;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void insertUsers() {
        userRepository.saveAll(
                Lists.newArrayList(
                        new User(
                                "james",
                                "admin",
                                "ROLE_" + UserRole.ADMIN),
                        new User(
                                "bob",
                                "user",
                                "ROLE_" + UserRole.USER)));
    }

    @Test
    public void findByUsernameTest() {
        User user = userRepository.findByUsername("james")
                .get();

        assertEquals(1L, user.getId().longValue());
        assertEquals(user.getUsername(), "james");
        assertEquals(user.getPassword(), "admin");
        assertEquals(user.getRole(), "ROLE_ADMIN");
    }
}
