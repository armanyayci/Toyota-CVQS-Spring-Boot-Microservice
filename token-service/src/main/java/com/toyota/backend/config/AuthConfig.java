package com.toyota.backend.config;

import com.toyota.backend.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/** @author Arman Yaycı
 * @since 01-05-2023
 * This class is a configuration class for Spring Security authentication.
 * It configures authentication provider, user details service, and password encoder.
 * It also configures security filter chain and authentication manager.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {

    /**
    * Repository for managing user data.
    */
    private final UserRepository userRepository;

    /**
     * Creates a new CustomUserDetailsService object.
     * @return CustomUserDetailsService object
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService(userRepository);
    }
    /**
     * Configures the security filter chain.
     * Paths which start with /auth/ are accessible. Other paths are authenticated.
     * @param http HttpSecurity object
     * @return SecurityFilterChain object
     * @throws Exception: if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .build();
    }
    /**
     * Configures the password encoder.
     * @return a new instance of the {@link BCryptPasswordEncoder} class
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider.
     * it serves to check the accuracy of user information
     * @return AuthenticationProvider object
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    /**
     * Configures the authentication manager.
     * @param config AuthenticationConfiguration object
     * @return AuthenticationManager object
     * @throws Exception if an error occurs while configuring the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
