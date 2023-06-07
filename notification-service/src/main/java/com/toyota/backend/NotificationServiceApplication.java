package com.toyota.backend;

import com.toyota.backend.event.RegisteredUserEvent;
import com.toyota.backend.service.MailService;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.KafkaListener;
/**
 * @author Arman
 * @since 07.06.2023
 * This class represents the Notification Service Application.
 * It is responsible for sending email notifications to registered users.
 */
@SpringBootApplication
@EnableEurekaClient
public class NotificationServiceApplication {

    private final Logger logger = Logger.getLogger(NotificationServiceApplication.class);
    private final MailService service;
    /**
     * Constructs a new NotificationServiceApplication with the specified MailService.
     * @param service the MailService used to send email notifications
     */
    public NotificationServiceApplication(MailService service) {
        this.service = service;
    }
    /**
     * The main method of the NotificationServiceApplication.
     * It starts the application by running the Spring Boot application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
    /**
     * Kafka listener method for processing registered user events.
     * It sends an email notification to the registered user.
     * @param events the RegisteredUserEvent received from the Kafka topic
     */
    @KafkaListener(topics = "notificationTopic", groupId = "notificationId")
    public void notificationHandler(RegisteredUserEvent events){
        //send email notification
        service.sendEmail(events.getEmail(), events.getName(), events.getUsername(), events.getPassword());
        logger.info(String.format("mail send successfully to %s",events.getEmail()));
    }
}