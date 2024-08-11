package com.banking.backend.service;

import com.banking.backend.Entity.LoginHistory;
import com.banking.backend.Entity.User;
import com.banking.backend.repository.LoginHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    public void saveLoginTime(User user) {
        LoginHistory loginHistory = new LoginHistory(user, LocalDateTime.now());
        loginHistoryRepository.save(loginHistory);
    }

    public void saveLogoutTime(User user) {
        try {
            LoginHistory loginHistory = loginHistoryRepository.findTopByUserOrderByLoginTimeDesc(user)
                .orElseThrow(() -> new Exception("No login history found for user: " + user.getId()));
            
            if (loginHistory.getLogoutTime() == null) {
                loginHistory.setLogoutTime(LocalDateTime.now());
                loginHistoryRepository.save(loginHistory);
            }
        } catch (Exception e) {
            // Log the error or handle it accordingly
            System.err.println("Error saving logout time: " + e.getMessage());
        }
    }

}
