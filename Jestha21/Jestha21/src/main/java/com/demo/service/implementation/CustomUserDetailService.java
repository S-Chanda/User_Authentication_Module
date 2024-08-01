package com.demo.service.implementation;


import com.demo.dto.ServerResponse;
import com.demo.dto.UserDto;
import com.demo.entity.User;
import com.demo.mapper.ResponseMapper;
import com.demo.mapper.UserMapper;
import com.demo.repository.UserRepository;
import com.demo.twoFA.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TwoFactorAuthenticationService tfaService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //load user from database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!") );
        return user;
    }

    public ServerResponse loadCurrentUser(String username){
        UserDetails userDetails = loadUserByUsername(username);
        return ResponseMapper.successResponse("User found", userDetails);
    }
}
