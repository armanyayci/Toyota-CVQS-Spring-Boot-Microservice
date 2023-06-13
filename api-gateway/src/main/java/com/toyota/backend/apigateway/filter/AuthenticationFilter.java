package com.toyota.backend.apigateway.filter;

import com.toyota.backend.apigateway.Util.JwtUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Arman Yaycı
 * @since 01-05-2023
 * This class is responsible for authenticating incoming requests by validating JWT tokens.
 * It acts as a filter in the gateway, intercepting requests and enforcing authentication and authorization rules.
 * The class extends the AbstractGatewayFilterFactory class to provide custom filtering behavior.
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);
    private final RouteValidator validator;
    private final JwtUtil jwtUtil;
    private final WebClient.Builder webClient;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil, WebClient.Builder webClient) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
        this.webClient = webClient;
    }

    /**
     * Applies a GatewayFilter to validate and authorize requests based on JWT authentication.
     * This filter checks the authorization header of the HTTP request, communicates with a token service to validate the token,
     * and verifies the roles claimed in the token to determine access to specific endpoints.
     * @param config The configuration for the GatewayFilter
     * @return The GatewayFilter that validates and authorizes requests
     * @exception ResponseStatusException unauthorized status when user does not have role to access the endpoint, HTTP request does not contain authorization header and token couldn't validate
     */
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //Checks the Authorization header of the HTTP request
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.info("HTTP request does not contain an authorization header.");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                String finalAuthHeader = authHeader;
                // Communicating with token-service to validate the token
                return tokenServiceRequest(authHeader)
                        .flatMap(isValid -> {
                            if (!isValid) {
                                logger.info(String.format("According to the token-service response, the token couldn't be validated. username: %s", jwtUtil.getUsernameFromToken(finalAuthHeader)));
                                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                            }
                            // Roles were claimed to token when generating,
                            // and now it is parsing to get the list of roles in order to access the endpoints.
                            List<String> roles = jwtUtil.parseTokenGetRoles(finalAuthHeader);
                            String username = jwtUtil.getUsernameFromToken(finalAuthHeader);

                            // ~/terminal/** endpoints do not need a role to access
                            // any user who has any role can access this point.
                            if (exchange.getRequest().getURI().getPath().startsWith("/terminal/")) {
                                if (roles.isEmpty()) {
                                    logger.fatal(String.format("user: %s does not have a role. ", username));
                                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                                }
                            }
                            // ~/admin/** endpoints need the admin role to access
                            else if (exchange.getRequest().getURI().getPath().startsWith("/admin/")) {
                                if (!roles.contains("ROLE_ADMIN")) {
                                    logger.info(String.format("user: %s has no admin role to access /admin/** endpoint", username));
                                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                                }
                            }
                            // ~/operator/** endpoints need the operator role to access
                            else if (exchange.getRequest().getURI().getPath().startsWith("/operator/")) {
                                if (!roles.contains("ROLE_OPERATOR")) {
                                    logger.info(String.format("user: %s has no operator role to access /operator/** endpoint", username));
                                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                                }
                            }
                            // ~/teamleader/** endpoints need the teamleader role to access
                            else if (exchange.getRequest().getURI().getPath().startsWith("/teamleader/")) {
                                if (!roles.contains("ROLE_TEAMLEADER")) {
                                    logger.info(String.format("user: %s has no teamleader role to access /teamleader/** endpoint", username));
                                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                                }
                            } else {
                                logger.debug("invalid endpoint access");
                                return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
                            }
                            return chain.filter(exchange);
                        });
            } else {
                return chain.filter(exchange);
            }
        });
    }
    public Mono<Boolean> tokenServiceRequest(String token){
        return webClient.build().get()
                .uri("http://token-service/auth/check/"+token)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public static class Config {

    }
}
