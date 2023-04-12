package toyotabackend.toyotabackend.service.Concrete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.AuthenticationException;
import toyotabackend.toyotabackend.TestUtils;
import toyotabackend.toyotabackend.dao.UserRepository;
import toyotabackend.toyotabackend.domain.Entity.User;
import toyotabackend.toyotabackend.dto.request.LoginDTO;
import toyotabackend.toyotabackend.dto.response.JwtResponse;
import toyotabackend.toyotabackend.security.JwtTokenProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest extends TestUtils {

    private AuthenticationService authenticationService;
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
        authenticationService = new AuthenticationService(userRepository,authenticationManager,jwtTokenProvider);
    }


    @Test
    public void auth_whenCalledWithValidLoginDto_itShouldReturnJwtResponse(){

        User user = generateUser();
        LoginDTO dto = generateLoginDto();
        JwtResponse response = generateJwtResponse();


        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(user)).thenReturn(response.getToken());

        JwtResponse result = authenticationService.auth(dto);

        assertEquals(result,response);
        verify(userRepository,times(1)).findByusername(dto.getUsername());
        verify(authenticationManager,times(1)).authenticate(new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword()));
        verify(jwtTokenProvider,times(1)).generateToken(user);
    }
    @Test
    public void auth_whenCalledWithNotExistUsername_itShouldThrowNullPointerException(){
        LoginDTO dto = generateLoginDto();
        User user = generateUser();

        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class,() -> authenticationService.auth(dto));

        verify(userRepository,times(1)).findByusername(dto.getUsername());
        verify(jwtTokenProvider,times(0)).generateToken(user);
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
}
































