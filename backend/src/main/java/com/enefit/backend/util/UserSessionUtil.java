package com.enefit.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UserSessionUtil {
    private final HttpServletRequest request;

    public UserSessionUtil(HttpServletRequest request) {
        this.request = request;
    }

    public Long getCurrentUserId() {
        return (Long) request.getSession().getAttribute("user");
    }
}
