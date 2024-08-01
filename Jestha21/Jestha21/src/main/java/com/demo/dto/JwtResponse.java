package com.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    String jwtToken;
    String username;
    boolean mfaEnabled;
}
