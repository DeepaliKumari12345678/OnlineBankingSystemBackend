package com.banking.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.backend.Entity.Account;

import java.util.List;
import java.util.Optional;
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId); // Fetch accounts based on userId
}
