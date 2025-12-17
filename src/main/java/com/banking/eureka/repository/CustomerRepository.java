package com.banking.eureka.repository;

import com.banking.eureka.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);
    Optional<User> findByAccountNumber(String accountNumber);


    boolean existsByEmail(
            @NotBlank(message = "Email is required")
            @Email(message = "Invalid email format")
            String email);
}
