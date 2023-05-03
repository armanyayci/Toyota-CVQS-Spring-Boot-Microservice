package com.toyota.backend;

import com.toyota.backend.domain.Role;
import com.toyota.backend.domain.User;
import com.toyota.backend.dto.request.LoginDTO;
import com.toyota.backend.dto.response.JwtResponse;
import com.toyota.backend.service.Concrete.AuthenticationServiceImpl;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Util class that helps to tests
 */
public class TestUtils {

    public static User generateUser() {
        return User.builder()
                .id(1)
                .name("Test")
                .email("test@test.com")
                .isActive(true)
                .password("testPassword")
                .username("testUsername")
                .roles(new ArrayList<>())
                .build();
    }

    public static LoginDTO generateLoginDto() {
        return LoginDTO.builder()
                .username("testUsername")
                .password("testPassword")
                .build();
    }

    public static JwtResponse generateJwtResponse() {
        return JwtResponse.builder()
                .Token("token")
                .build();
    }
    public static Role generateRole(){
        return Role.builder()
                .id(1)
                .name("testRole")
                .users(new ArrayList<>())
                .build();
    }
}
