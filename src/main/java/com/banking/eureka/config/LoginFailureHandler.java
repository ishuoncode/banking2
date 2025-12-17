package com.banking.eureka.config;

import com.banking.eureka.entity.Status;
import com.banking.eureka.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Data
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final CustomerRepository customerRepository;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {

        String email = request.getParameter("username");

        if (email != null) {
            customerRepository.findByEmail(email).ifPresent(user -> {

                int attempts = user.getFailedAttempts() + 1;
                user.setFailedAttempts(attempts);

                if (attempts >= 3) {
                    user.setStatus(Status.INACTIVE);
                }

                customerRepository.save(user);
            });
        }

        response.sendRedirect("/login?error&username=" + email);
    }
}
