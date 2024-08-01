package com.demo.dto;

import com.demo.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordTokenDto {

    private Long tokenId;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 10; // in minutes
    private User user;

    public static Date getTokenExpirationTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime()); // gets current milliseconds
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME); // add expiration time to current time
        // creating a new Date object using the time from the calendar object after the expiration time has been added.
        // This Date object represents the future time when the token will expire.
        return new Date(calendar.getTime().getTime());
    }
}
