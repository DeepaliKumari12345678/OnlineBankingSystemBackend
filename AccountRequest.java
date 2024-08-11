package com.banking.backend.dto;


import lombok.Data;

import java.math.BigDecimal;


public class AccountRequest {
    
    private String accountNumber;
    private BigDecimal balance;
    public Long getUserId() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setUserId(Long userId) {
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
