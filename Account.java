package com.banking.backend.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    
    private BigDecimal balance;

    @ManyToOne
    private User user;

    private static final Random random = new Random();

    // Default Constructor
    public Account(User user) {
        this.user = user;
        this.setAccountNumber(generateAccountNumber());
        this.setBalance(generateInitialBalance());
    }

    // Generate a random account number with a configurable prefix
    public static String generateAccountNumber() {
        return "ACC-" + RandomStringUtils.randomNumeric(11);
    }

    // Generate a random initial balance
    public static BigDecimal generateInitialBalance() {
        BigDecimal minBalance = new BigDecimal("100.00"); // Example minimum balance
        BigDecimal maxBalance = new BigDecimal("10000.00"); // Example maximum balance
        BigDecimal randomBalance = minBalance.add(new BigDecimal(random.nextDouble())
            .multiply(maxBalance.subtract(minBalance)));
        return randomBalance.setScale(2, RoundingMode.HALF_UP);
    }

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
