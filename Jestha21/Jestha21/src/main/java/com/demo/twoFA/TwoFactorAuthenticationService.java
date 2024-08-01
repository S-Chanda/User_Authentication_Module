package com.demo.twoFA;

import org.springframework.stereotype.Service;


public interface TwoFactorAuthenticationService {
    public String generateSecret();

    public String generateQrCodeImageUri(String secret);

    public boolean isOtpValid(String secret, String code);




}
