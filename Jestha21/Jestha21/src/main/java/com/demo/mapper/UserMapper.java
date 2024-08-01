package com.demo.mapper;

import com.demo.dto.UserDto;
import com.demo.entity.User;

public class UserMapper {
    public static UserDto getUserDto(User user){
        UserDto dto = new UserDto();
        dto.setUserID(dto.getUserID());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setUserRole(user.getUserRole());
        dto.setMfaEnabled(user.isMfaEnabled());
        return dto;
    }

    public static User getUserEntity(UserDto userDto){
        User user = new User();
        user.setUserID(user.getUserID());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setUserRole(userDto.getUserRole());
        user.setMfaEnabled(userDto.isMfaEnabled());
        return user;
    }
}
