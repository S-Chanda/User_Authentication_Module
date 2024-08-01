package com.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @Positive(message = "User Id must be positive")
    private long userId;
    @NotEmpty(message = "Old Password is required")
    @NotBlank(message = "Old Password cannot be blank")
    private String oldPassword;

    @NotEmpty(message = "New Password is required")
    @NotBlank(message = "New Password cannot be blank")
    @Size(min = 6, message = "New Password must be at least 6 characters long")
    private String newPassword;
}
