package com.toyota.backend.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/** @author Arman Yaycı
 * @since 01-05-2023
 * WebClientConfig class provides communication between microservices.
 * It includes WebClient.Builder class which is coming from spring-starter-WebFlux
*/
@Configuration
public class WebClientConfig {

    /**
     * Provides a fluent API for building and configuring instances of the WebClient class.
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
