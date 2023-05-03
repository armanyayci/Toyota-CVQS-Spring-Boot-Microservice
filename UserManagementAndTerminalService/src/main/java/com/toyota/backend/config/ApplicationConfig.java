package com.toyota.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Configuration class for the application.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    /**
     * Creates a new instance of the {@link BCryptPasswordEncoder} class.
     * @return a new instance of the {@link BCryptPasswordEncoder} class
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
