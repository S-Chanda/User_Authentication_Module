package com.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 10; // in minutes

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ForgotPasswordToken(User user, String token) {
        this.user = user;
        this.token = token;
        this.expirationTime= this.getTokenExpirationTime();
    }

     public Date getTokenExpirationTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime()); // gets current milliseconds
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME); // add expiration time to current time
        // creating a new Date object using the time from the calendar object after the expiration time has been added.
        // This Date object represents the future time when the token will expire.
        return new Date(calendar.getTime().getTime());
    }

}
