package com.banking.backend.utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.banking.backend.service.UserService;

@Component
public class PasswordUpdater implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        userService.hashExistingPasswords(); // Call the method to re-hash passwords
        System.out.println("Existing passwords have been hashed.");
    }
}

