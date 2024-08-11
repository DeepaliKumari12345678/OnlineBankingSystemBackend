package com.banking.backend.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.backend.Entity.Account;
import com.banking.backend.Entity.User;
import com.banking.backend.dto.AccountRequest;
import com.banking.backend.service.AccountService;
import com.banking.backend.service.LoginHistoryService;
import com.banking.backend.service.UserService;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private AccountService accountService;
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isAuthenticated = userService.authenticate(user.getUsername(), user.getPassword());
            if (isAuthenticated) {
                Optional<User> optionalAuthenticatedUser = userService.findByUsername(user.getUsername());
                if (optionalAuthenticatedUser.isPresent()) {
                    User authenticatedUser = optionalAuthenticatedUser.get();
                    loginHistoryService.saveLoginTime(authenticatedUser);
                    
                    String token = "your_generated_jwt_token"; // Replace with actual JWT generation logic
                    response.put("message", "Login successful");
                    response.put("token", token);
                    response.put("userId", authenticatedUser.getId()); // Return user ID
                    return ResponseEntity.ok(response);
                }
            } else {
                response.put("error", "Invalid username or password");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.status(401).body(response); // Handle invalid login case
    }
    @PostMapping("api/account/create")
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

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        Long Id = Long.valueOf(request.get("Id")); // Assuming you send the user ID as a string

        try {
            User authenticatedUser = userService.findById(Id).orElseThrow(() -> new Exception("User not found"));
            loginHistoryService.saveLogoutTime(authenticatedUser);

            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @PostMapping("/account/{accountId}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        try {
            Account account = accountService.deposit(accountId, amount);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle error scenarios
        }
    }

    @PostMapping("/account/{accountId}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        try {
            Account account = accountService.withdraw(accountId, amount);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null); // Handle insufficient balance scenario
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle other error scenarios
        }
    }

    @GetMapping("/account")
    public ResponseEntity<Optional<List<Account>>> getAccounts(@RequestParam Long userId) {
        try {
            Optional<List<Account>> accounts = Optional.of(accountService.getAccountsByUserId(userId));
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        try {
            // Check if the user exists using the UserService
            Optional<User> optionalUser = userService.findUserByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                // Generate a password reset token (this is just an example)
                String token = UUID.randomUUID().toString();
                // Store the token in the database or send an email with the link
                // Example: userService.createPasswordResetToken(user, token);
                
                response.put("message", "Password reset link has been sent to your email.");
            } else {
                response.put("error", "User with this email does not exist.");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok("User registered successfully. Please check your email for further instructions.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}
