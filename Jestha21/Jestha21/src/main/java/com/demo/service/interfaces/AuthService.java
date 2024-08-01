package com.demo.service.interfaces;

import com.demo.dto.ServerResponse;
import com.demo.entity.User;
import com.demo.twoFA.VerficationRequest;
import org.springframework.stereotype.Service;


public interface AuthService {

    public ServerResponse generateQrForOtp(Long id);

    public ServerResponse verifyOtp(VerficationRequest verficationRequest);
}
