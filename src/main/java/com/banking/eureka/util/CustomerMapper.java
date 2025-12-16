package com.banking.eureka.util;

import com.banking.eureka.dto.CustomerResponseDTO;
import com.banking.eureka.dto.DashboardDTO;
import com.banking.eureka.entity.User;

public class CustomerMapper {

    private CustomerMapper() {}

    public static CustomerResponseDTO toResponse(User user) {

        return CustomerResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .balance(user.getBalance())
                .status(user.getStatus().name())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .pincode(user.getPincode())
                .build();
    }


    public static DashboardDTO toDashboard(User user) {

        return DashboardDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .city(user.getCity())
                .status(user.getStatus().name())
                .balance(user.getBalance())
                .build();
    }
}

