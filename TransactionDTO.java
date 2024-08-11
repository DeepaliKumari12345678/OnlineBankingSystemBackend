package com.banking.backend.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private LocalDateTime timestamp;
    private BigDecimal amount;
    private String type;
    private Long accountId;
}
