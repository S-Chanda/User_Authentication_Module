package com.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServerResponse implements Serializable { // implemented for caching
    boolean success;
    String message;
    Object data;
}
