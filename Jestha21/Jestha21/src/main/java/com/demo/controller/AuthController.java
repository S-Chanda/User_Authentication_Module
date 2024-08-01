package com.demo.controller;

import com.demo.dto.JwtRequest;
import com.demo.dto.JwtResponse;
import com.demo.dto.ServerResponse;
import com.demo.entity.User;
import com.demo.security.JwtHelper;
import com.demo.service.implementation.CustomUserDetailService;
import com.demo.service.interfaces.AuthService;
import com.demo.twoFA.VerficationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    CustomUserDetailService userDetailService;

    @Autowired
    JwtHelper helper;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        //authenticate user by Authentication Manager
        this.doAuthenticate(request.getUsername(), request.getPassword());
        //get user from UserDetailService
        UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername());

        //generate token
        String token = helper.generateToken(userDetails);

        //now send generated token to user

        // check 2-factor enabled
        User user = (User) userDetails;
        if (user.isMfaEnabled()) {
            JwtResponse response = JwtResponse.builder().jwtToken(token).mfaEnabled(true).username(userDetails.getUsername()).build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {

            JwtResponse response = JwtResponse.builder().jwtToken(token).mfaEnabled(false).username(userDetails.getUsername()).build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        }


    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password"); //this will be shown on console
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid"; //this will be shown in response
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ServerResponse> verifyOtpCode(@RequestBody VerficationRequest verificationRequest) {
        ServerResponse response = authService.verifyOtp(verificationRequest);
        System.out.println("Verify OTP executed");
        System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/generateQr/{id}")
    public ResponseEntity<ServerResponse> generateQr(@PathVariable Long id) {
        ServerResponse response = authService.generateQrForOtp(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
