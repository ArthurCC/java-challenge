package jp.co.axa.apidemo.model;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom UserDetails model used by Spring Security to keep track of an
 * authenticated user
 * 
 * @author Arthur Campos Costa
 */
public class CustomUserDetails implements UserDetails {

    /** username */
    private final String username;

    /** password */
    private final String password;

    /** authorities */
    private final Set<? extends GrantedAuthority> authorities;

    /**
     * Constructor
     * 
     * @param username    username
     * @param password    password
     * @param authorities authorities
     */
    public CustomUserDetails(
            String username,
            String password,
            Set<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public String getUsername() {
        return username;
    }

    // Below fields logic not implemented due to lack of time
    /**
     * {@inheritDoc}}
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
