package com.toyota.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import com.toyota.backend.dto.request.LoginDTO;
import com.toyota.backend.dto.response.JwtResponse;
import com.toyota.backend.service.Concrete.AuthenticationServiceImpl;

import javax.validation.Valid;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 *Rest controller class that handles authentication-related requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;
    /**
     * Handles login request with the given user credentials and returns a JWT token if the credentials are valid.
     * @param loginDTO User login credentials
     * @return ResponseEntity containing the JWT token in the body if the credentials are valid, else returns HttpStatus.UNAUTHORIZED
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@Valid @RequestBody LoginDTO loginDTO){
        try{
            return ResponseEntity.ok(authenticationService.auth(loginDTO));
        }
        catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Checks if the given JWT token is valid or not.
     * @param token JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    @GetMapping("/check/{token}")
    public boolean checkValidationOfToken (@PathVariable("token") String token){
        return authenticationService.isValid(token);
    }
}





















