package jp.co.axa.apidemo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.axa.apidemo.entities.User;

/**
 * user repository interface. Used by our custom userDetailsService to retrieve
 * a user by username for authentication
 * 
 * @author Arthur Campos Costa
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Get a user by username. Spring generates SQL query from method name so we
     * don't need to write it
     * 
     * @param username username
     * @return Optional of User
     */
    Optional<User> findByUsername(String username);
}
