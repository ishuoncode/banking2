package com.banking.eureka.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    private String name;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    private String password;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private int failedAttempts = 0;


    private String address;
    private String city;
    private String state;
    private String pincode;
}
