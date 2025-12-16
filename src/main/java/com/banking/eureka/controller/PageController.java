package com.banking.eureka.controller;

import com.banking.eureka.dto.CreateCustomerDTO;
import com.banking.eureka.dto.DashboardDTO;
import com.banking.eureka.dto.LoginRequestDTO;
import com.banking.eureka.entity.User;
import com.banking.eureka.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final CustomerService customerService;


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequestDTO", new LoginRequestDTO());
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("createCustomerDTO", new CreateCustomerDTO());
        return "signup";
    }


    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute CreateCustomerDTO dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return "signup";
        }

        customerService.createCustomer(dto);
        return "redirect:/login";
    }



    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        String email = authentication.getName();

        DashboardDTO dashboard = customerService.getCustomerDashboardByEmail(email);

        model.addAttribute("user", dashboard);

        return "dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(
            @RequestParam BigDecimal amount,
            Authentication authentication) {

        String email = authentication.getName();
        customerService.deposit(email, amount);

        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(
            @RequestParam BigDecimal amount,
            Authentication authentication) {

        String email = authentication.getName();
        customerService.withdraw(email, amount);

        return "redirect:/dashboard";
    }



}
