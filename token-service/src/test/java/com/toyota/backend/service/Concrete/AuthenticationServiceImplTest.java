package com.toyota.backend.service.Concrete;

import com.toyota.backend.TestUtils;
import com.toyota.backend.dao.UserRepository;
import com.toyota.backend.domain.Role;
import com.toyota.backend.domain.User;
import com.toyota.backend.dto.request.LoginDTO;
import com.toyota.backend.dto.response.JwtResponse;
import com.toyota.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link AuthenticationServiceImpl} class.
 */
@SpringBootTest
class AuthenticationServiceImplTest extends TestUtils {

    private AuthenticationServiceImpl authenticationService;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private Authentication authentication;
    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
        userRepository = mock(UserRepository.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        authenticationService = new AuthenticationServiceImpl(userRepository,authenticationManager,jwtTokenProvider);
    }
    @Test
    public void auth_whenCalledWithValidLoginDto_itShouldReturnJwtResponse(){

        User user = generateUser();
        Role role = generateRole();
        user.getRoles().add(role);
        LoginDTO dto = generateLoginDto();
        JwtResponse response = generateJwtResponse();

        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword()))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(user, List.of(role.getName()))).thenReturn(response.getToken());

        authenticationService.auth(dto);

        verify(userRepository,times(1)).findByusername(dto.getUsername());
        verify(authenticationManager,times(1)).authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword()));
        verify(jwtTokenProvider,times(1)).generateToken(user,List.of(role.getName()));
    }
    @Test
    public void auth_whenCalledWithNotExistUsername_itShouldThrowNullPointerException(){
        LoginDTO dto = generateLoginDto();
        User user = generateUser();
        Role role = generateRole();

        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class,() -> authenticationService.auth(dto));

        verify(userRepository,times(1)).findByusername(dto.getUsername());
        verify(jwtTokenProvider,times(0)).generateToken(user,List.of(role.getName()));
        verify(authenticationManager,times(0)).authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword()));
    }

    @Test
    void auth_whenCalledWithInvalidPassword_itShouldThrowAuthenticationException() {

        LoginDTO dto = generateLoginDto();
        User user = generateUser();
        user.setPassword("invalidPassword");

        when(userRepository.findByusername(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())))
                .thenThrow(new AuthenticationException("Password is not correct.") {});

        assertThrows(AuthenticationException.class, () -> authenticationService.auth(dto));
        assertNotEquals(user.getPassword(),dto.getPassword());
    }
    @Test
    void isValid_whenCalledWithToken_itShouldReturnBoolean(){

        String token="token";
        User user = generateUser();
        LoginDTO dto = generateLoginDto();
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn(dto.getUsername());
        when(userRepository.getActiveUserByUsername(dto.getUsername())).thenReturn(Optional.of(user));

        authenticationService.isValid(token);
        verify(userRepository,times(1)).getActiveUserByUsername(dto.getUsername());
        verify(jwtTokenProvider,times(1)).getUsernameFromToken(token);
    }
    @Test
    void isValid_whenCalledWithNotExistUserOrDeleted_itShouldThrowNullPointerException(){

        String token="token";
        LoginDTO dto = generateLoginDto();
        when(jwtTokenProvider.getUsernameFromToken(token)).thenReturn(dto.getUsername());
        when(userRepository.getActiveUserByUsername(dto.getUsername())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> authenticationService.isValid(token));
        verify(userRepository,times(0)).getActiveUserByUsername(token);
    }
}