package com.demo.controller;

import com.demo.dto.ChangePasswordDto;
import com.demo.dto.ResetPasswordDto;
import com.demo.dto.ServerResponse;
import com.demo.dto.UserDto;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.entity.UserRole;
import com.demo.mapper.ResponseMapper;
import com.demo.mapper.UserMapper;
import com.demo.service.implementation.CustomUserDetailService;
import com.demo.service.interfaces.ForgotPasswordService;
import com.demo.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CustomUserDetailService userDetailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ForgotPasswordService passwordService;

    @PostMapping("/addUser")
    public ResponseEntity<ServerResponse> addNormalUser(@Valid @RequestBody UserDto userDto, BindingResult result){

        if(result.hasErrors()){
            String error = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            ServerResponse serverResponse = ResponseMapper.failureResponse(error);
            return ResponseEntity.badRequest().body(serverResponse);
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Set<UserRole> userRoleSet = new HashSet<>();
        Role role = new Role();
        role.setRoleID(45L); //if role ID not set explicitly, new row is formed in Role table on each execution
        role.setRoleName("NORMAL");

        User user = UserMapper.getUserEntity(userDto);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleSet.add(userRole);

        ServerResponse serverResponse = userService.createUser(userDto,userRoleSet);
        return ResponseEntity.ok(serverResponse);
    }

    @GetMapping("/current-user")
    public ResponseEntity<ServerResponse> getCurrentUser(Principal principal){
        ServerResponse response = this.userDetailService.loadCurrentUser(principal.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerResponse> getUserById(@PathVariable Long id){
        ServerResponse response = this.userService.getUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<ServerResponse> getAllUsers(){
        ServerResponse serverResponse = this.userService.getAllUsers();
        return new ResponseEntity<>(serverResponse, HttpStatus.OK);
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<ServerResponse> changePassword(@Valid @PathVariable("id") Long id, @RequestBody ChangePasswordDto passwordDto, BindingResult result){

        if(result.hasErrors()) {
            String errorMsg = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            ServerResponse response = ResponseMapper.failureResponse(errorMsg);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        passwordDto.setUserId(id);
        ServerResponse response = this.userService.changePassword(passwordDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
   public ResponseEntity<ServerResponse> deleteUser(@PathVariable Long id){
        ServerResponse response = userService.deleteUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<ServerResponse> updateUser(@PathVariable("id") Long id,  @RequestBody UserDto userDto){
        userDto.setUserID(id);
        ServerResponse response = userService.updateProfile(userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ServerResponse> forgotPassword(@RequestBody Map<String, String> emailPayload, HttpServletRequest request){
        String email = emailPayload.get("email");
        ServerResponse response = passwordService.forgotPassword(email, request);
        System.out.println(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ServerResponse> resetPassword(@RequestBody ResetPasswordDto passwordDto, @RequestParam("token") String token){
        ServerResponse response = passwordService.resetPassword(passwordDto, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }






}