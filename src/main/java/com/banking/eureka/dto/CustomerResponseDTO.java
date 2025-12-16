package com.banking.eureka.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CustomerResponseDTO {

    private Long userId;
    private String name;
    private String email;

    private BigDecimal balance;
    private String status;

    private String address;
    private String city;
    private String state;
    private String pincode;
}
