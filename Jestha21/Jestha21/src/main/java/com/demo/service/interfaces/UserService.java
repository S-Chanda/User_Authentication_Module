package com.demo.service.interfaces;

import com.demo.dto.ChangePasswordDto;
import com.demo.dto.ServerResponse;
import com.demo.dto.UserDto;
import com.demo.entity.User;
import com.demo.entity.UserRole;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

public interface UserService {
    public ServerResponse createUser(UserDto userDto, Set<UserRole> userRoleSet);

    public ServerResponse getAllUsers();

    public ServerResponse getUserById(Long id);

    public ServerResponse changePassword(ChangePasswordDto passwordDto);

    public ServerResponse deleteUser(Long id);

    public ServerResponse updateProfile(UserDto userDto);




}
