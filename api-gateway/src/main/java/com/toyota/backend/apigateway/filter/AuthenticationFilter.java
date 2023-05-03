package com.toyota.backend.apigateway.filter;

import com.toyota.backend.apigateway.Util.JwtUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

/** @author Arman Yaycı
 * @since 01-05-2023
 *
 * It provides configurations such as which filters to pass requests
 * to be made via Api-gateway (jwt token, role control of endpoints).
 * <p>
 * Require the instances of RouteValidator, JwtUtil, WebClient.Builder
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private static Logger logger = Logger.getLogger(AuthenticationFilter.class);
    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private WebClient.Builder webClient;

    public AuthenticationFilter() {
        super(Config.class);
    }

    /**
     * The AuthenticationFilter class is extended with the
     * AbstractGatewayFilterFactory class and special configurations are defined.
     * @param config -> which is inner static class.
     * <p>
     *  Apply method of GatewayFilter class controls the rules related
     * to the JWT structure (includes authorization?, starts with the bearer?).
     * <p>
     * Besides the JWT structure, it provides
     * the access to endpoints with proper roles.
     * <p>
     * @exception RuntimeException : when HTTP request does not contain authorization header
     * @exception RuntimeException : when token couldn't validate in /token-service/
     * @exception ResponseStatusException -> unauthorized status when user does not have role to access the endpoint
     * <p>
     * @return chain.filter(exchange) : enables processing of request by sequentially applying other filters in the chain
     */
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //Checks the Authorization header of the HTTP request
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.warn(String.format("HTTP request does not contain authorization header. Request details: %s", exchange.getRequest()));
                    throw new RuntimeException();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                //communicating with token-service to validate the token
                Mono<Boolean> isValid = webClient.build().get()
                            .uri("http://token-service/auth/check/"+authHeader)
                            .retrieve()
                            .bodyToMono(Boolean.class);

                String finalAuthHeader = authHeader;
                isValid.subscribe(result -> {
                    if(!result) {
                        logger.warn(String.format("According to the token-service response, token couldn't validated. TokenDetails: %s", finalAuthHeader));
                        throw new RuntimeException();
                    }});

                // Roles were claimed to token when generating,
                // and now it is parsing to get the list of roles in order to access the endpoints.
                List<String> roles = jwtUtil.parseTokenGetRoles(authHeader);

                // ~/terminal/** endpoints does not need role to access
                // user who has any role can access this point.
                if (exchange.getRequest().getURI().getPath().startsWith("/terminal/")){
                    if (roles.isEmpty()){
                        String username = jwtUtil.getUsernameFromToken(authHeader);
                        logger.fatal(String.format("user: %s does not have role. ", username));
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                    }
                }
                // ~/admin/** endpoints needs admin role to access
                else if (exchange.getRequest().getURI().getPath().startsWith("/admin/") ){
                    if (!roles.contains("ROLE_ADMIN")){
                        String username = jwtUtil.getUsernameFromToken(authHeader);
                        logger.info(String.format("user: %s has no admin role to access /admin/** endpoint",username));
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                    }
                }
                // ~/operator/** endpoints needs admin role to access
                else if (exchange.getRequest().getURI().getPath().startsWith("/operator/")){
                    if (!roles.contains("ROLE_OPERATOR")){
                        String username = jwtUtil.getUsernameFromToken(authHeader);
                        logger.info(String.format("user: %s has no operator role to access /operator/** endpoint",username));
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                    }
                }
                // ~/teamleader/** endpoints needs admin role to access
                else if (exchange.getRequest().getURI().getPath().startsWith("/teamleader/")){
                    if (!roles.contains("ROLE_TEAMLEADER")){
                        String username = jwtUtil.getUsernameFromToken(authHeader);
                        logger.info(String.format("user: %s has no teamleader role to access /teamleader/** endpoint",username));
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                    }
                }
                else {
                    logger.debug("invalid endpoint access");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
            return chain.filter(exchange);
        });
    }
    public static class Config {

    }
}
