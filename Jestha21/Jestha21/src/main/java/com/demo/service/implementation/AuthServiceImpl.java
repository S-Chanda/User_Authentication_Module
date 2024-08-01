package com.demo.service.implementation;

import com.demo.dto.ServerResponse;
import com.demo.entity.User;
import com.demo.mapper.ResponseMapper;
import com.demo.repository.UserRepository;
import com.demo.security.JwtHelper;
import com.demo.service.interfaces.AuthService;
import com.demo.twoFA.TwoFactorAuthenticationService;
import com.demo.twoFA.VerficationRequest;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TwoFactorAuthenticationService tfaService;

    @Autowired
    JwtHelper jwtHelper;

    @Override
    public ServerResponse generateQrForOtp(Long id) {
        User user = userRepository.findById(id).get();
        String qrCode = tfaService.generateQrCodeImageUri(user.getSecret());
        return ResponseMapper.successResponse("QR generated success", qrCode);

    }

    @Override
    public ServerResponse verifyOtp(VerficationRequest verficationRequest) {
        User user = userRepository.findByEmail(verficationRequest.getEmail());
        if(Optional.ofNullable(user).isEmpty()){

            return ResponseMapper.failureResponse("User with given email not found");
        }else{
            if(!tfaService.isOtpValid(user.getSecret(), verficationRequest.getCode())){
                return ResponseMapper.failureResponse("Invalid Token please try again.");
            }


            return ResponseMapper.successResponse("Two factor authentication set successfully", user);
        }

    }

//    public String generateOtp(String email){
//            User user = userRepository.findByEmail(email);
//            if(user==null){
//                System.out.println("User repo not found");
//                return null;
//            }
//            else{
//                CodeGenerator codeGenerator = new DefaultCodeGenerator();
//                try {
//                    String otp = codeGenerator.generate(user.getSecret(), (System.currentTimeMillis()/1000)/30); // time is given in seconds
//                    System.out.println("Generated OTP code: " + otp);
//                    return otp;
//                } catch (CodeGenerationException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//    }
}
