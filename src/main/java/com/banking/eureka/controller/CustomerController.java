package com.banking.eureka.controller;

import com.banking.eureka.dto.CreateCustomerDTO;
import com.banking.eureka.dto.CustomerResponseDTO;
import com.banking.eureka.dto.LoginRequestDTO;
import com.banking.eureka.dto.LoginResponseDTO;
import com.banking.eureka.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }


    @PostMapping("/signup")
    public ResponseEntity<CustomerResponseDTO> signup(
            @Valid @RequestBody CreateCustomerDTO dto) {
        System.out.println("Signup DTO = " + dto);

        CustomerResponseDTO response = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        LoginResponseDTO response = customerService.login(dto);
        return ResponseEntity.ok(response);
    }



}
