package com.banking.eureka.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardDTO {
    private String accountNumber;
    private String name;
    private String email;
    private String city;
    private String status;
    private BigDecimal balance;
}
