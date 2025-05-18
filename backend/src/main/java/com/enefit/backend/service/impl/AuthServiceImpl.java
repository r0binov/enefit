package com.enefit.backend.service.impl;

import com.enefit.backend.dto.SuccessResponseDto;
import com.enefit.backend.entity.Customer;
import com.enefit.backend.repository.CustomerRepository;
import com.enefit.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SuccessResponseDto login(String username, String password, HttpServletRequest request) {
        Customer customer = customerRepository.findCustomerByUserName(username);
        SuccessResponseDto response = new SuccessResponseDto();
        if (customer == null || !passwordEncoder.matches(password, customer.getPassword())) {
            response.setStatus(401);
            response.setData("Invalid username or password");
            return response;
        }
        request.getSession(true).setAttribute("user", customer.getId());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        customer.getUserName(), null, java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        response.setStatus(200);
        response.setData("Login successful");
        return response;
    }

    @Override
    public SuccessResponseDto logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        SuccessResponseDto response = new SuccessResponseDto();
        if (session != null) {
            session.invalidate();
        }
        response.setStatus(200);
        response.setData("Logout successful");
        SecurityContextHolder.clearContext();
        return response;
    }
}
