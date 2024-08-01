package com.demo.service.implementation;

import com.demo.dto.ServerResponse;
import com.demo.dto.UserDto;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // to enable mock annotations
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void createUserTest_ReturnServerResponse() {
        User user = User.builder().username("Aman").email("aman@test.com").password("aman123").build();
        UserDto userDto = UserDto.builder().username("Aarati").email("aarati@test.com").password("aarati123").build();

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        ServerResponse savedUserDto = userService.createUser2(userDto);

        Assertions.assertThat(savedUserDto).isNotNull();
    }


    @Test
    void getAllUsersTest() {
        userService.getAllUsers();
        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void getUserByIdTest_ReturnServerResponse(){
        User user = User.builder().username("Aman").email("aman@test.com").password("aman123").build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        ServerResponse savedUser = userService.getUserById(1L);
        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void deleteUserTest_ReturnNull(){
        // If userService.deleteUser(1L) throws an exception, it will be caught and reported by the assertAll method. If it doesn't throw any exceptions, the assertion will pass. If there were more lambda expressions inside the assertAll method, all of them would be executed, and any failures would be reported together.
        org.junit.jupiter.api.Assertions.assertAll(()-> userService.deleteUser(1L));


    }
}