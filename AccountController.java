package com.banking.backend.controller;

import com.banking.backend.Entity.Account;
import com.banking.backend.service.AccountService;
import com.banking.backend.dto.AccountRequest; // Assuming you have this DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest accountRequest) {
        try {
            // Logic to create an account
            Account newAccount = accountService.createAccount(accountRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
        } catch (Exception e) {
            // Log the exception and return a meaningful error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            Account account = accountService.deposit(id, amount);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle error scenarios
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            Account account = accountService.withdraw(id, amount);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null); // Handle insufficient balance scenario
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle other error scenarios
        }
    }
}
