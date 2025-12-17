package com.banking.eureka.config;

import com.banking.eureka.entity.User;
import com.banking.eureka.repository.CustomerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final CustomerRepository customerRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        String email = authentication.getName();

        User user = customerRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setFailedAttempts(0);   // ✅ RESET HERE
            customerRepository.save(user);
        }

        response.sendRedirect("/dashboard");
    }
}
