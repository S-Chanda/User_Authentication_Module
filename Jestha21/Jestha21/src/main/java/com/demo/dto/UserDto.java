package com.demo.dto;

import com.demo.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto implements Serializable {
    private Long userID;
    @NotEmpty(message = "Username is required")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotEmpty(message = "Email is required")
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    private Set<UserRole> userRole = new HashSet<>();

    // for 2FA
    private boolean isMfaEnabled;
    private String secretImageUri;
}
