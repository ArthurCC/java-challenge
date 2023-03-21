package jp.co.axa.apidemo.services.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.model.CustomUserDetails;
import jp.co.axa.apidemo.repositories.UserRepository;

/**
 * Custom user details service used by Spring Security for authenticating users
 * 
 * @author Arthur Campos Costa
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    /** user repository */
    private final UserRepository userRepository;

    /**
     * Constructor
     * 
     * @param userRepository user repository
     */
    public CustomUserDetailsServiceImpl(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by username
     * 
     * @param username username
     * @return UserDetails instance representing the logged in user
     * @throws UsernameNotFoundException if user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                Sets.newHashSet(new SimpleGrantedAuthority(user.getRole())));
    }
}
