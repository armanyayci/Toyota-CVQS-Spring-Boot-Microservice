package com.toyota.backend.service.Concrete;

import com.toyota.backend.domain.Role;
import com.toyota.backend.service.Abstract.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import com.toyota.backend.dao.UserRepository;
import com.toyota.backend.domain.User;
import com.toyota.backend.dto.request.LoginDTO;
import com.toyota.backend.dto.response.JwtResponse;
import com.toyota.backend.security.JwtTokenProvider;

import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Implementation of the {@link AuthenticationService} interface,
 * provides methods to handle authentication related tasks.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = Logger.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authenticates user with the provided login credentials, and generates a JWT token if authentication succeeds.
     * @param loginDto The {@link LoginDTO} object containing the login credentials.
     * @return A {@link JwtResponse} object containing the generated JWT token.
     * @throws AuthenticationException If the login credentials are invalid.
     */
    @Override
    public JwtResponse auth(LoginDTO loginDto){

        try {
            //get user by username
            User user = userRepository.findByusername(loginDto.getUsername())
                    .orElseThrow(
                            ()-> new NullPointerException(String.format("username not found : %s",loginDto.getUsername())));
            //authenticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginDto.getUsername(), loginDto.getPassword()));
            //get user role list.
            List <String> roles = user.getRoles().stream().map(Role::getName).toList();
            //generate token
            String token = jwtTokenProvider.generateToken(user,roles);

            logger.info(String.format("User: %s is authenticated",user.getUsername()));
            return JwtResponse.builder().Token(token).build();
        }
        catch (AuthenticationException e){
            logger.debug(String.format("Password is not correct. username: %s",loginDto.getUsername()));
            throw e;
        }
    }
    /**
     * Checks if the provided JWT token is valid and not expired.
     * @param token The JWT token to check.
     * @return True if the token is valid and not expired, false otherwise.
     */
    @Override
    public boolean isValid(String token){
        //get username from token
        String username = jwtTokenProvider.getUsernameFromToken(token);

        //find user by username
        User user = userRepository.findByusername(username).orElseThrow(
                ()-> new NullPointerException(String.format("user not found in token with username: %s", username)));
        return !jwtTokenProvider.isTokenExpired(token);
    }
}

