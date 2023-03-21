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

/**
 * Test class for UserRepository to test custom request
 * 
 * Using DataJpaTest to create in memory DB and related application context for
 * this test scope
 * 
 * @author Arthur Campos Costa
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

        /** user repositoty, subject */
        @Autowired
        private UserRepository userRepository;

        /**
         * Initialize DB with arbitrary users
         */
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

        /**
         * Test findByUsername query
         */
        @Test
        public void findByUsernameTest() {
                // test
                User user = userRepository.findByUsername("james")
                                .get();

                // assert
                assertEquals(1L, user.getId().longValue());
                assertEquals(user.getUsername(), "james");
                assertEquals(user.getPassword(), "admin");
                assertEquals(user.getRole(), "ROLE_ADMIN");
        }
}
