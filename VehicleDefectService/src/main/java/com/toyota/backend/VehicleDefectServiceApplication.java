package com.toyota.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class VehicleDefectServiceApplication {
    public static void main(String[] args) {

        // opencv library should be implemented to project to draw lines on image.
        // download the library and give the local path "~/opencv/build/java/x64/opencv_java470.dll".
        // more information https://opencv.org/releases/
        System.load("C:/Users/arman/Desktop/opencv/build/java/x64/opencv_java470.dll");
        SpringApplication.run(VehicleDefectServiceApplication.class, args);
    }
}