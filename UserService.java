package com.banking.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.banking.backend.Entity.LoginHistory;
import com.banking.backend.Entity.User;
import com.banking.backend.repository.LoginHistoryRepository; // Ensure this import is present
import com.banking.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Repository for user database operations

    @Autowired
    private LoginHistoryRepository loginHistoryRepository; // Repository for login history

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Password encoder

    // Method to save a new user
    public void save(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        System.out.println("User registered: " + user.getUsername()); // Log the registration
    }

    // Method to authenticate a user
    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username); // Retrieve user from database
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Compare the stored hashed password with the provided password
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false; // User not found
    }

    // Record login time for a user
    public void recordLogin(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            LoginHistory loginHistory = new LoginHistory(user, LocalDateTime.now());
            loginHistoryRepository.save(loginHistory);
        }
    }

    // Record logout time for a user
    public void recordLogout(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<LoginHistory> optionalLoginHistory = loginHistoryRepository.findAll()
                    .stream()
                    .filter(lh -> lh.getUser().getId().equals(user.getId()) && lh.getLogoutTime() == null)
                    .findFirst();
            if (optionalLoginHistory.isPresent()) {
                LoginHistory loginHistory = optionalLoginHistory.get();
                loginHistory.setLogoutTime(LocalDateTime.now());
                loginHistoryRepository.save(loginHistory);
            }
        }
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    // Find a user by email
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findById(Long Id) {
        return userRepository.findById(Id);
    }
    // Method to re-hash existing passwords
    public void hashExistingPasswords() {
        List<User> users = userRepository.findAll(); // Retrieve all users
        for (User user : users) {
            // If the password does not match the BCrypt format, re-hash it
            if (!user.getPassword().startsWith("$2a") && !user.getPassword().startsWith("$2b") && !user.getPassword().startsWith("$2y")) {
                String hashedPassword = passwordEncoder.encode(user.getPassword()); // Re-hash password
                user.setPassword(hashedPassword);
                userRepository.save(user); // Save updated user
                System.out.println("Rehashed password for user: " + user.getUsername());
            }
        }
    }
}
