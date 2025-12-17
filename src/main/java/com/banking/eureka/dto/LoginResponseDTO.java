package com.banking.eureka.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoginResponseDTO {

    private Long userId;
    private String accountNumber;
    private String name;
    private String email;
    private String status;
}
