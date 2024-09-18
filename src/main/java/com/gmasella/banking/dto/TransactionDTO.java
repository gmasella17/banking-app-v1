package com.gmasella.banking.dto;

import com.gmasella.banking.service.TransactionType;

import java.time.LocalDateTime;

public record TransactionDTO(Long id, Long accountId, double amount, TransactionType transactionType,
                             LocalDateTime timestamp) {
}
