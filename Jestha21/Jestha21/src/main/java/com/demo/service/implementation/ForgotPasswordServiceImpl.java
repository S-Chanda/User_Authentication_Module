package com.demo.service.implementation;

import com.demo.dto.ResetPasswordDto;
import com.demo.dto.ServerResponse;
import com.demo.entity.ForgotPasswordToken;
import com.demo.entity.User;
import com.demo.mapper.ResponseMapper;
import com.demo.repository.ForgotPasswordTokenRepository;
import com.demo.repository.UserRepository;
import com.demo.service.interfaces.ForgotPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final String REDIRECTION_URL = "http://localhost:4200/reset-password?token=";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForgotPasswordTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    // to send password reset link if user forgets the password
    public ServerResponse forgotPassword(String email, final HttpServletRequest request){

        User user = userRepository.findByEmail(email);
        String passwordRestUrl ="";
        if(user!=null){
            String passwordResetToken = UUID.randomUUID().toString();

            // saving the password token
            ForgotPasswordToken token = new ForgotPasswordToken(user, passwordResetToken);
            tokenRepository.save(token);

            passwordRestUrl = sendPasswordResetEmailLink(user, fetchApplicationUrl(request), passwordResetToken);
            return ResponseMapper.successResponse("Please check your email for Password Reset Link", passwordRestUrl) ;

        }else{
            return ResponseMapper.failureResponse("Couldn't find your email. Please try again.");
        }


    }

    public String sendPasswordResetEmailLink(User user, String applicationUrl, String passwordResetToken ){
        String url = applicationUrl+ "/user/reset-password?token="+passwordResetToken;


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Reset your Password!");
        mailMessage.setText("To reset your account, please click here : " + REDIRECTION_URL + passwordResetToken);
        this.emailService.sendEmail(mailMessage);
        System.out.println("Click on link to reset your password" + url);
        return url;
    }



    public ServerResponse resetPassword(ResetPasswordDto passwordDto, String token){
        boolean isTokenValid =  this.validatePasswordRestToken(token);
        if(isTokenValid){
            User user = tokenRepository.findByToken(token).getUser();
            if(user!=null){
                this.updateUserPassword(user, passwordDto.getNewPassword());
                ForgotPasswordToken token1 = tokenRepository.findByToken(token);
                tokenRepository.delete(token1);
                return ResponseMapper.successResponse("Password Reset Success", user);
            }
            else{
                return ResponseMapper.failureResponse("User with given token not found");
            }
        }
        else{
            return ResponseMapper.failureResponse("Invalid Password Reset Token");
        }

    }

    public void updateUserPassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }



    public String fetchApplicationUrl(HttpServletRequest request) {
        String var10000 = request.getServerName();
        return "http://" + var10000 + ":" + request.getServerPort() + request.getContextPath();
    }


    public boolean validatePasswordRestToken(String token ){
        ForgotPasswordToken tokenObj = tokenRepository.findByToken(token);

        if (tokenObj == null) {
            return false;
        }
        else{
            User user = tokenObj.getUser();
            Calendar calendar = Calendar.getInstance();
            boolean isLinkExpired = tokenObj.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0L;
            if (isLinkExpired) {
                System.out.println("Link already expired, Resend link.");
                return false;
            } else {
                return true;
            }
        }
    }

    public User findUserByPasswordToken(String token){
       Optional<User> optionalUser =  Optional.ofNullable(tokenRepository.findByToken(token).getUser());
       if(optionalUser.isPresent()){
           return optionalUser.get();
       }else{
           return null;
       }
    }
}
