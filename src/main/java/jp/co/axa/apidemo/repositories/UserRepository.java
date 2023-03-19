package jp.co.axa.apidemo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.axa.apidemo.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
