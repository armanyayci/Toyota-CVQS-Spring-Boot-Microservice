package com.toyota.backend.service.Abstract;

public interface MailService {
    void sendEmail(String email, String name,String username, String password);
}
