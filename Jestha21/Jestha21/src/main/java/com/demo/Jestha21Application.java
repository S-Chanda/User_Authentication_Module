package com.demo;

import com.demo.dto.UserDto;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.entity.UserRole;
import com.demo.mapper.UserMapper;
import com.demo.repository.UserRepository;
import com.demo.service.implementation.AuthServiceImpl;
import com.demo.service.interfaces.UserService;
import com.demo.twoFA.TwoFactorAuthenticationServiceImpl;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableCaching // for Redis caching
public class Jestha21Application implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(Jestha21Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Running...");


	}


}
