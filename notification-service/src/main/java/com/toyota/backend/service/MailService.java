package com.toyota.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Arman Yaycı
 * @since 07.06.2023
 * This class represents a MailService that is used to send confirmation emails for registration.
 * It utilizes the JavaMailSender to send emails.
 */
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    public String subject = "TOYOTA CVQS REGISTER CONFIRMATION";
    public String text = "Dear %s, \nOur team members have successfully completed the registration process. Your login credentials:\nusername: %s \npassword: %s\nKind Regards." ;

    /**
     * Sends a confirmation email to the specified email address with the provided user information.
     * @param email The email address to send the notification email to.
     * @param name The name of the registered user.
     * @param username The username for the registered account.
     * @param password The password for the registered account.
     */
    public void sendEmail(String email, String name,String username, String password){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("armantoyotatest@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(String.format(text,name,username,password));
        mailSender.send(message);
    }
}


