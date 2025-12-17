package com.banking.eureka.controller;

import com.banking.eureka.dto.CreateCustomerDTO;
import com.banking.eureka.dto.DashboardDTO;
import com.banking.eureka.dto.TransferByAccountDTO;
import com.banking.eureka.entity.Status;
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
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "username", required = false) String username,
            Model model) {
        System.out.println(username+"😊😊😊😊");
        if (error != null && username != null) {
            try {
                User user = customerService.getCustomerByEmail(username);

                int remaining = 3 - user.getFailedAttempts();

                if (user.getStatus() == Status.INACTIVE) {
                    model.addAttribute("errorMessage", "Your account is locked");
                } else if (remaining > 0) {
                    model.addAttribute(
                            "errorMessage",
                            remaining + " login attempt(s) left"
                    );
                } else {
                    model.addAttribute("errorMessage", "Invalid email or password");
                }

            } catch (RuntimeException ex) {
                model.addAttribute("errorMessage", "Invalid email or password");
            }

        }

        System.out.println(model.toString()+"😊😊😊😊");
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

        @PostMapping("/transfer")
        public String transfer(
                @Valid @ModelAttribute TransferByAccountDTO dto,
                org.springframework.security.core.Authentication authentication) {

            String email = authentication.getName();

            User sender = customerService.getCustomerByEmail(email);

            customerService.transferByAccountNumber(
                    sender.getAccountNumber(),
                    dto.getToAccountNumber(),
                    dto.getAmount()
            );

            return "redirect:/dashboard";
        }
    }




