package jp.co.axa.apidemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Lists;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.enumeration.UserRole;
import jp.co.axa.apidemo.repositories.UserRepository;

/**
 * Spring Security configuration class
 * 
 * @author Arthur Campos Costa
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // TODO : might not need that anymore
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    /** user details service for authentication */
    private final UserDetailsService userDetailsService;

    /** enable security flag */
    private final boolean enableSecurity;

    /**
     * Constructor
     * 
     * @param userDetailsService user details service
     * @param enableSecurity     enable security flag, defaults to true
     */
    public WebSecurityConfig(
            UserDetailsService userDetailsService,
            @Value("${app.security.enabled:true}") boolean enableSecurity) {
        this.userDetailsService = userDetailsService;
        this.enableSecurity = enableSecurity;
    }

    /**
     * Inject our custom userDetailsService in Authentication Manager
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Configure http security and endpoint accesses.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // POST, PUT, DELETE operations only accessible by admin
                .antMatchers(HttpMethod.POST, "/api/v1/**")
                .hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/v1/**")
                .hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/v1/**")
                .hasRole(UserRole.ADMIN.name())
                // GET operations accessible by admin and user
                .antMatchers("/api/v1/**")
                .hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
                .and()
                // Use of httpBasic for demonstration purposes, we would not use this in
                // production if our API is public since it's easy to decrypt http basic
                // credentials. In that case, JWT would be more appropriate
                .httpBasic();
    }

    /**
     * Configure web security
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        if (enableSecurity) {
            // disable security for H2 console
            web
                    .ignoring()
                    .antMatchers("/h2-console/**");
        } else {
            // completely disable security
            web
                    .ignoring()
                    .antMatchers("/**");
        }
    }

    /**
     * Password encoder bean
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ApplicationRunner Bean that inserts 2 fake users on application startup for
     * authentication purposes.
     * 
     * @param userRepository  user repository
     * @param passwordEncoder password encoder
     * @return ApplicationRunner implementation instance
     */
    @Bean
    ApplicationRunner createFakeUsersRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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
