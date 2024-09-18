package com.gmasella.banking.dto;

public record TransferFundsDTO(Long fromAccountId,
                               Long toAccountId,
                               double amount) {
}
