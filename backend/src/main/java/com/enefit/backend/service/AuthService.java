package com.enefit.backend.service;

import com.enefit.backend.dto.SuccessResponseDto;
import jakarta.servlet.http.HttpServletRequest;


public interface AuthService {
    SuccessResponseDto login(String username, String password, HttpServletRequest request);
    SuccessResponseDto logout(HttpServletRequest request);
}
