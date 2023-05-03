package com.toyota.backend.config;

import com.toyota.backend.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class is a custom implementation of the Spring Security UserDetailsService interface.
 * It loads user details from the UserRepository.
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * Repository for managing user data.
     */
    private final UserRepository userRepository;

    /**
     Loads user details by username from the UserRepository.
     @param username the username of the user to load
     @return UserDetails object representing the user
     @throws NullPointerException if the user is not found in the UserRepository
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByusername(username).orElseThrow(
                ()-> new NullPointerException(String.format("user not found with username: %s",username)));
    }
}
