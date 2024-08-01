package com.demo.service.implementation;

import com.demo.dto.ChangePasswordDto;
import com.demo.dto.ServerResponse;
import com.demo.dto.UserDto;
import com.demo.entity.User;
import com.demo.entity.UserRole;
import com.demo.mapper.ResponseMapper;
import com.demo.mapper.UserMapper;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;
import com.demo.service.interfaces.UserService;
import com.demo.twoFA.TwoFactorAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TwoFactorAuthenticationService tfaService;


    // for testing purpose
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // @CachePut(value = "user", key = "#result.data.userID") // result refers to response returned by method
    @Override
    public ServerResponse createUser(UserDto userDto, Set<UserRole> userRoleSet) {

        User getUser = UserMapper.getUserEntity(userDto);
        Optional<User> existingUser = userRepository.findByUsername(getUser.getUsername());
        if (existingUser.isPresent()) {
            return ResponseMapper.failureResponse("User already present!");
        } else {

            User savedUser = userRepository.save(getUser);
            System.out.println(savedUser.isMfaEnabled());
            // if mfaEnabled --> Generate Secret
            if (savedUser.isMfaEnabled()) {
                savedUser.setSecret(tfaService.generateSecret());
            }


            for (UserRole ur : userRoleSet) {
                ur.setUser(savedUser);
                roleRepository.save(ur.getRole());
            }
            //adding roles to user
            savedUser.getUserRole().addAll(userRoleSet);




            //saving user again

            User savingUser = userRepository.save(savedUser);
            userDto.setUserID(savingUser.getUserID());
            userDto.setSecretImageUri(tfaService.generateQrCodeImageUri(savingUser.getSecret()));
            System.out.println("User id: " + userDto.getUserID());
            ServerResponse response = ResponseMapper.successResponse("User added successfully!", userDto);
            return response;
        }

    }

    @Cacheable(value = "user")
    @Override
    public ServerResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseMapper.successResponse("All users: ", users);
    }

    @Cacheable(value = "user", key = "#id") // key should be method parameter and is unique
    @Override
    public ServerResponse getUserById(Long id) {
        System.out.println("Getting user from database..."); // to check caching
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = UserMapper.getUserDto(user);
            return ResponseMapper.successResponse("User found", userDto);
        } else {
            return ResponseMapper.failureResponse("User with " + id + " not found.");
        }

    }


    // for testing only
    public ServerResponse createUser2(UserDto userDto) {

        User getUser = UserMapper.getUserEntity(userDto);
        User local = userRepository.findByUsername(getUser.getUsername()).get();
        if (local != null) {
            return ResponseMapper.failureResponse("User already present!");
        } else {
            User savedUser = userRepository.save(getUser);

        }
        return ResponseMapper.successResponse("User added successfully!", local);
    }

    // for testing
    // @CacheEvict(value = "user", key = "#id")
    @Caching(
            evict = {@CacheEvict(value = "user", allEntries = true), @CacheEvict(value = "user", key = "#id")}
    )
    public ServerResponse deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return ResponseMapper.successResponse("User with Id: " + id + " deleted successfully", null);
        } else {
            return ResponseMapper.failureResponse("User with Id: " + id + " not found");
        }

    }

    public ServerResponse changePassword(ChangePasswordDto passwordDto) {
        Optional<User> optionalUser = userRepository.findById(passwordDto.getUserId());
        if (!optionalUser.isPresent()) {
            return ResponseMapper.failureResponse("User with given id not found");
        } else {
            User existingUser = optionalUser.get();
            if (checkAuthenticatedUser(existingUser.getUsername())) {
                boolean checkOldPassword = passwordEncoder.matches(passwordDto.getOldPassword(), existingUser.getPassword());
                if (checkOldPassword) {
                    existingUser.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
                    this.userRepository.save(existingUser);
                    return ResponseMapper.successResponse("Password changed successfully", existingUser);
                } else {
                    return ResponseMapper.failureResponse("Incorrect Old Password");
                }
            } else {
                return ResponseMapper.failureResponse("Source and Target users doesn't match");
            }

        }
    }

    public boolean checkAuthenticatedUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipleName = authentication.getName();
        if (username.equals(currentPrincipleName)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public ServerResponse updateProfile(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userDto.getUserID());
        if (!optionalUser.isPresent()) {
            return ResponseMapper.failureResponse("User ID not found");
        } else {
            User existingUser = optionalUser.get();
            if (checkAuthenticatedUser(existingUser.getUsername())) {
                if (userDto.getUsername() != null) {
                    User existUsername = userRepository.findByUsername(userDto.getUsername()).get();
                    if (existUsername == null || existUsername.getUserID() == userDto.getUserID()) {
                        existingUser.setUsername(userDto.getUsername());
                    } else {
                        return ResponseMapper.failureResponse("Username already present !");
                    }
                }
                if (userDto.getEmail() != null) {
                    existingUser.setEmail(userDto.getEmail());
                }

                User user = userRepository.save(existingUser);
                UserDto savedUser = UserMapper.getUserDto(user);
                return ResponseMapper.successResponse("User updated success", savedUser);
            } else {
                return ResponseMapper.failureResponse("Source and Target Users doesn't match.");
            }
        }
    }




}
