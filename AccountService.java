package com.banking.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.backend.Entity.Account;
import com.banking.backend.Entity.User;
import com.banking.backend.dto.AccountRequest;
import com.banking.backend.repository.AccountRepository;
import com.banking.backend.repository.UserRepository;

import graphql.com.google.common.base.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public void save(Account account) {
        accountRepository.save(account);
    }

    public java.util.Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Account deposit(Long id, BigDecimal amount) {
        java.util.Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            accountRepository.save(account);
            return account;
        }
        throw new RuntimeException("Account not found.");
    }

    public Account createAccount(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account(user);
        account.setAccountNumber(Account.generateAccountNumber()); // Make sure this method exists
        account.setBalance(Account.generateInitialBalance()); // Make sure this method exists

        return accountRepository.save(account);
    }

    public Account withdraw(Long id, BigDecimal amount) {
        java.util.Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (account.getBalance().compareTo(amount) >= 0) {
                BigDecimal newBalance = account.getBalance().subtract(amount);
                account.setBalance(newBalance);
                accountRepository.save(account);
                return account;
            } else {
                throw new IllegalArgumentException("Insufficient balance.");
            }
        }
        throw new RuntimeException("Account not found.");
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId); // Use the repository method
    }
}
