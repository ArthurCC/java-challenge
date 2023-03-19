package jp.co.axa.apidemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Lists;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.enumeration.UserRole;
import jp.co.axa.apidemo.repositories.UserRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class ApiDemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ApiDemoApplication.class, args);
	}

	@Bean
	public ApplicationRunner createFakeUsersRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			userRepository.saveAll(
					Lists.newArrayList(
							new User(
									"james",
									passwordEncoder.encode("admin"),
									"ROLE_" + UserRole.ADMIN),
							new User(
									"bob",
									passwordEncoder.encode("user"),
									"ROLE_" + UserRole.USER)));

			LOGGER.info(
					"Created fake users for authentication : ADMIN[username=james,password=admin] USER[username=bob,password=user]");
		};
	}
}
