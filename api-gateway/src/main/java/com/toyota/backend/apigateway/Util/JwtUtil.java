package com.toyota.backend.apigateway.Util;

import com.toyota.backend.apigateway.filter.AuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * JwtTokenProvider is a component used for handling JWT tokens. It includes methods for retrieving
 * tokens, and checking if a token has expired. It also includes methods for extracting information from the token,
 * such as the username and expiration date.
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final static Logger logger = Logger.getLogger(JwtUtil.class);
    @Value("${toyota.app.secret}")
    private String APP_SECRET;
    /**
     * Retrieves the username from the JWT token.
     * @param token the JWT token
     * @return the username extracted from the token
     * @throws Exception if there is an error extracting the username
     */
    public String getUsernameFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (Exception UsernameNotFoundException) {
            logger.warn("Invalid username while getting from token");
            throw UsernameNotFoundException;
        }
    }

    /**
     * Retrieves a claim from the JWT token.
     * @param token the JWT token
     * @param claimsResolver the function used to extract the claim from the token
     * @param <T> the type of the claim
     * @return the claim extracted from the token
     * @throws Exception if there is an error extracting the claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.warn("claims couldnt get from the token with t function");
            throw e;
        }
    }
    /**
     * Retrieves all claims from the JWT token by parsing the token and getting the body of the claims.
     * @param token the JWT token to retrieve claims from.
     * @return a Claims object containing all the claims from the token.
     * @throws Exception if there was an error parsing the token or getting the claims from it.
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.warn("couldnt get the informations from token with secret key");
            throw e;
        }
    }
    /**
     * Parses the given JWT token and returns a list of roles extracted from the token.
     * @param token the JWT token to parse
     * @return a list of roles extracted from the token
     * @throws Exception if the token cannot be parsed or if an error occurs while extracting the role claims
     */
    public List<String> parseTokenGetRoles(String token) {
        try {
            return (List<String>) Jwts
                    .parser()
                    .setSigningKey(APP_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles");
        } catch (Exception e) {
            logger.warn(String.format("Token couldn't parsed in order to get the role claims. Token: %s ",token));
            throw e;
        }
    }
}