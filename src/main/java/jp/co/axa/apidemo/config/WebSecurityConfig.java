package jp.co.axa.apidemo.config;

import org.springframework.beans.factory.annotation.Value;
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

import jp.co.axa.apidemo.enumeration.UserRole;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final boolean enableSecurity;

    public WebSecurityConfig(
            UserDetailsService userDetailsService,
            @Value("${app.security.enabled:true}") boolean enableSecurity) {
        this.userDetailsService = userDetailsService;
        this.enableSecurity = enableSecurity;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/**")
                .hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/v1/**")
                .hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/v1/**")
                .hasRole(UserRole.ADMIN.name())
                .antMatchers("/api/v1/**")
                .hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
                .and()
                .httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if (enableSecurity) {
            // disable security for H2 console
            web
                    .ignoring()
                    .antMatchers("/h2-console/**");
        } else {
            web
                    .ignoring()
                    .antMatchers("/**");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
