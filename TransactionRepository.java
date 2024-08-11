package com.banking.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.backend.Entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
}

