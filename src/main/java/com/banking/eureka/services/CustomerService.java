package com.banking.eureka.services;

import com.banking.eureka.dto.*;
import com.banking.eureka.entity.Status;
import com.banking.eureka.entity.User;
import com.banking.eureka.repository.CustomerRepository;
import com.banking.eureka.util.AccountNumberGenerator;
import com.banking.eureka.util.CustomerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // SIGNUP
    public CustomerResponseDTO createCustomer(CreateCustomerDTO dto) {

        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (customerRepository.existsByAccountNumber(accountNumber));

        User user = User.builder()
                .accountNumber(accountNumber)
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .balance(BigDecimal.ZERO)
                .status(Status.ACTIVE)
                .build();

        User saved = customerRepository.save(user);
        return CustomerMapper.toResponse(saved);
    }

    // LOGIN
    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Account is locked. Please contact support.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);
            if (attempts >= 3) {
                user.setStatus(Status.INACTIVE);
            }
            customerRepository.save(user);

            throw new RuntimeException(
                    attempts >= 3
                            ? "Account locked due to multiple failed attempts"
                            : "Invalid email or password"
            );
        }

        user.setFailedAttempts(0);
        customerRepository.save(user);

        return LoginResponseDTO.builder()
                .accountNumber(user.getAccountNumber())
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .build();
    }

    public DashboardDTO getCustomerDashboardByEmail(String email) {

        User user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return CustomerMapper.toDashboard(user);
    }




    @Transactional
    public void deposit(String email, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        User user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBalance(user.getBalance().add(amount));

        customerRepository.save(user);
    }

    @Transactional
    public void withdraw(String email, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdraw amount must be positive");
        }

        User user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance().subtract(amount));

        customerRepository.save(user);
    }




    @Transactional
    public void transferByAccountNumber(
            String fromAccountNumber,
            String toAccountNumber,
            BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        User sender = customerRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = customerRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }


        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        customerRepository.save(sender);
        customerRepository.save(receiver);
    }

    public User getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
