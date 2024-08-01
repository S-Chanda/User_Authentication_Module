package com.demo.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


//this class sends mail to the recipient
@Service("emailService")
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.mailSender=javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email){
        mailSender.send(email);
    }





}
