package com.toyota.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * JwtTokenProvider is a component used for handling JWT tokens. It includes methods for generating and retrieving
 * tokens, and checking if a token has expired. It also includes methods for extracting information from the token,
 * such as the username and expiration date.
 */
@Component
public class JwtTokenProvider {
    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class);

    // It represents the secret key that will be used during the JWT token production.
    @Value("${toyota.app.secret}")
    private String APP_SECRET;

    //specifies the validity period of a valid JWT token
    @Value("${toyota.expires.in}")
    private long EXPIRES_IN;
    /**
     * Retrieves the username from the JWT token.
     * @param token the JWT token
     * @return the username extracted from the token
     * @throws Exception if there is an error extracting the username
     */
    public String getUsernameFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (Exception e) {
            logger.warn(String.format("Username couldn't get from token: %s",token));
            throw e;
        }

    }

    /**
     * Retrieves the expiration date from the JWT token.
     * @param token the JWT token
     * @return the expiration date extracted from the token
     * @throws Exception if there is an error extracting the expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getExpiration);
        } catch (Exception e) {
            logger.warn(String.format("The expirations couldn't get from the token: %s",token));
            throw e;
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
            logger.warn(String.format("Specified claim couldn't get from the token: %s",token));
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
            logger.warn(String.format("All claims couldn't get from token: %s",token));
            throw e;
        }
    }

    /**
     Checks if the JWT token has expired.
     @param token the JWT token
     @return true if the token has expired, false otherwise
     @throws Exception if there is an error checking if the token has expired
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.warn(String.format("isTokenExpired crushed with token: %s",token));
            throw e;
        }
    }

    /**
     Generates a JWT token for the given user details and list of roles.
     @param userDetails the user details
     @param roles the list of roles
     @return the generated JWT token
     @throws Exception if there is an error generating the token
     */
    public String generateToken(UserDetails userDetails, List<String> roles) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles",roles);
            return doGenerateToken(claims, userDetails.getUsername());
        } catch (Exception e) {
            logger.warn(String.format("Couldn't generate token with user details, username: %s, roles: %s",userDetails.getUsername(),roles));
            throw e;
        }
    }
    /**
     * Creates a JWT token with the given claims and subject.
     * <br>
     * -While creating the token -
     * <br>
     * 1- Defines  claims of the token, like Issuer, Expiration, Subject, and the ID
     * <br>
     * 2- Sign the JWT using the HS256 algorithm and secret key.
     * <br>
     * 3. According to JWS Compact Serialization(<a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1">...</a>)compaction of the JWT to a URL-safe string
     * While creating the token -
     * Defines  claims of the token, like Issuer, Expiration, Subject, and the ID
     * @param claims the claims to include in the token
     * @param subject the subject of the token
     * @return the generated JWT token
     * @throws Exception if there is an error generating the token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        try {
            return Jwts.builder()
                    .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN * 1000))
                    .signWith(SignatureAlgorithm.HS256, APP_SECRET).compact();
        } catch (Exception e) {
            logger.warn(String.format("couldnt dogeneratetoken username:%s ", subject));
            throw e;
        }
    }
}