package com.demo.mapper;

import com.demo.dto.ServerResponse;

public class ResponseMapper {

    public static ServerResponse successResponse(String message, Object data){
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setSuccess(true);
        serverResponse.setMessage(message);
        serverResponse.setData(data);
        return serverResponse;
    }

    public static ServerResponse failureResponse(String message){
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setSuccess(false);
        serverResponse.setMessage(message);
        return serverResponse;
    }
}

