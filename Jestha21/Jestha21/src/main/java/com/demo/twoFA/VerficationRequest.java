package com.demo.twoFA;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerficationRequest {

    private String email;
    private String code;
}
