package com.toyota.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UserManagementTerminalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserManagementTerminalServiceApplication.class, args);
    }
}