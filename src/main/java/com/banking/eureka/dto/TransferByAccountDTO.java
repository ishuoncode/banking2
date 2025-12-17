package com.banking.eureka.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferByAccountDTO {

    @NotBlank(message = "Receiver account number is required")
    private String toAccountNumber;

    @NotNull
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
