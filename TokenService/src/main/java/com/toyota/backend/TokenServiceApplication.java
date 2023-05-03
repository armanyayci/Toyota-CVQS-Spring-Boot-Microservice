package com.toyota.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TokenServiceApplication {
    public static void main(String[] args) {
        System.load("C:/Users/arman/Desktop/opencv/build/java/x64/opencv_java470.dll");
        SpringApplication.run(TokenServiceApplication.class, args);
    }
}