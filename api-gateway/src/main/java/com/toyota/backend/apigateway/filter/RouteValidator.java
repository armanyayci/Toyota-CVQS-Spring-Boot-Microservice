package com.toyota.backend.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/** @author Arman Yaycı
 * @since 01-05-2023
 * Provides a route validator, which checks whether
 * a given request is secured or not
 */
@Component
public class RouteValidator {

    /**
     * Defines a list of open API endpoints, which are endpoints
     * that do not require authentication or authorization to access.
     */
    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/eureka/**",
            "/auth/check/**"
    );

    /**
     * The isSecured predicate is defined to check whether a given
     * request is secured or not based on the requested URI. The predicate
     * checks whether the requested URI contains any of the paths in the list.
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}