package com.banking.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.backend.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // Include this if you don't have it already
}
