package toyotabackend.toyotabackend.security;

import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtTokenProvider {
    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class);
    @Value("${toyota.app.secret}")
    private String APP_SECRET;
    @Value("${toyota.expires.in}")
    private long EXPIRES_IN;


    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (Exception UsernameNotFoundException) {
            logger.warn("Invalid username while getting from token");
            throw UsernameNotFoundException;
        }

    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {

        try {
            return getClaimFromToken(token, Claims::getExpiration);
        } catch (Exception e) {

            logger.warn("the expirations couldn't get from the token");
            throw e;
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.warn("claims couldnt get from the token with t function");
            throw e;
        }

    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.warn("couldnt get the informations from token with secret key");
            throw e;
        }
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.warn("");
            throw e;
        }

    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        try {
            Map<String, Object> claims = new HashMap<>();
            return doGenerateToken(claims, userDetails.getUsername());
        } catch (Exception e) {
            logger.warn("couldn't generate token with user details ");
            throw e;
        }

    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        try {
            return Jwts.builder()
                    .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN * 1000))
                    .signWith(SignatureAlgorithm.HS256, APP_SECRET).compact();
        } catch (Exception e) {
            logger.warn("couldnt dogeneratetoken ");
            throw e;
        }

    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            logger.warn("couldnt validatetoken");
            throw e;
        }
    }
}

