package com.demo.dto;

import lombok.Data;

@Data
public class JwtRequest {
    String username;
    String password;
}
