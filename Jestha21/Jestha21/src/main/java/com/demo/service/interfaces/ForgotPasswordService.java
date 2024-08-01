package com.demo.service.interfaces;

import com.demo.dto.ResetPasswordDto;
import com.demo.dto.ServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface ForgotPasswordService {
    public ServerResponse forgotPassword(String email, final HttpServletRequest request);

    public ServerResponse resetPassword(ResetPasswordDto passwordDto, String token);
}