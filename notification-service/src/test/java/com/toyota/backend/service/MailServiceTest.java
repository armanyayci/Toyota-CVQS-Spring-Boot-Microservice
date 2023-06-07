package com.toyota.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
/**
 * @author Arman Yaycı
 * @since 07.06.2023
 * Unit tests for the {@link MailService} class.
 */
@SpringBootTest
class MailServiceTest {
    private MailService service;
    private JavaMailSender sender;
    @BeforeEach
    void SetUp(){
        sender= mock(JavaMailSender.class);
        service = new MailService(sender);
    }
    @Test
    void sendEmail_whenCalledWithProvidedRegisteredUserEventParameters_itShouldSendMail(){
        String email = "test@toyota.com";
        String name = "toyota test";
        String username = "toyotatest";
        String password = "test123";

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        service.sendEmail(email, name, username, password);

        verify(sender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals("armantoyotatest@gmail.com", sentMessage.getFrom());
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals("TOYOTA CVQS REGISTER CONFIRMATION", sentMessage.getSubject());
        assertEquals(
                String.format("Dear %s, \nOur team members have successfully completed the registration process. " +
                        "Your login credentials:\nusername: %s \npassword: %s\nKind Regards.", name, username, password),
                sentMessage.getText()
        );
    }
}