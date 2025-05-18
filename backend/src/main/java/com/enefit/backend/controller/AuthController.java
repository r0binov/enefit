package com.enefit.backend.controller;

import com.enefit.backend.dto.SuccessResponseDto;
import com.enefit.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request
    ) {
        SuccessResponseDto response = authService.login(username, password, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponseDto> logout(HttpServletRequest request) {
        SuccessResponseDto response = authService.logout(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}