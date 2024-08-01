package com.demo.repository;

import com.demo.entity.User;
import com.jayway.jsonpath.internal.Utils;
import net.bytebuddy.description.NamedElement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest// to inject dependencies
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest(){
        User user = User.builder()
                .username("Aman")
                .email("aman@test.com")
                .password("aman123")
                .build();
        User savedUser = userRepository.save(user);
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUserID()).isGreaterThan(0);
    }

    @Test
    public void findAllTest(){
        User user = User.builder()
                .username("Aman")
                .email("aman@test.com")
                .password("aman123")
                .build();

        User user2 = User.builder()
                .username("Aarati")
                .email("aarati@test.com")
                .password("aarati123")
                .build();
        userRepository.save(user);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();
        Assertions.assertThat(userList).isNotEmpty();
    }




    @Test
    void findById_returnsOneUser(){
        User user = User.builder()
                .username("Aman")
                .email("aman@test.com")
                .password("aman123")
                .build();

        userRepository.save(user);
        User returnedUser = userRepository.findById(user.getUserID()).get();
        Assertions.assertThat(returnedUser).isNotNull();
    }

    // testing custom query
    @Test
    void findByUsernameTest_ReturnUserNotNull(){
        User user = User.builder()
                .username("Aman")
                .email("aman@test.com")
                .password("aman123")
                .build();

        userRepository.save(user);
        User returnedUser = userRepository.findByUsername(user.getUsername()).get();
        Assertions.assertThat(returnedUser).isNotNull();
    }

    @Test
    void updateUser_ReturnUserNotNull(){
        User user = User.builder()
                .username("Avash")
                .email("aman@test.com")
                .password("aman123")
                .build();

        userRepository.save(user);
        User savedUser = userRepository.findById(user.getUserID()).get();
        savedUser.setUsername("Gita");
        User updatedUser = userRepository.save(savedUser);
        Assertions.assertThat(updatedUser.getUsername()).isNotNull();
    }

    @Test
    void deleteUserTest_ReturnUserIsEmpty(){
        User user = User.builder()
                .username("Aman")
                .email("aman@test.com")
                .password("aman123")
                .build();

        userRepository.save(user);
        userRepository.deleteById(user.getUserID());
        Optional<User> returnedUser = userRepository.findById(user.getUserID());
        Assertions.assertThat(returnedUser).isEmpty();
    }




}