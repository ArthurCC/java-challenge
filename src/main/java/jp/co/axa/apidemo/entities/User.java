package jp.co.axa.apidemo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User JPA entity used for authentication by our userDetailsService
 * 
 * @author Arthur Campos Costa
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "USER")
public class User {

    /**
     * Constructor
     * 
     * @param username username
     * @param password password
     * @param role     role
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** username */
    private String username;

    /** password */
    private String password;

    /** role */
    private String role;
}
