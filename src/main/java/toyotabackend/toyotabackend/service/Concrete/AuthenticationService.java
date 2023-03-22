package toyotabackend.toyotabackend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toyotabackend.toyotabackend.dao.RoleRepository;
import toyotabackend.toyotabackend.dao.UserRepository;
import toyotabackend.toyotabackend.domain.Entity.Role;
import toyotabackend.toyotabackend.domain.Entity.User;
import toyotabackend.toyotabackend.dto.request.LoginDTO;
import toyotabackend.toyotabackend.dto.request.RegisterDTO;
import toyotabackend.toyotabackend.dto.response.JwtResponse;
import toyotabackend.toyotabackend.security.JwtTokenProvider;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = Logger.getLogger(AuthenticationService.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public JwtResponse save(RegisterDTO registerDTO) {

        try {
            List<Role> roles = new ArrayList<>();

            roles.add(roleRepository.findById(registerDTO.getRoleId()).orElseThrow
                    (()-> new NullPointerException("Role not found with id "+ registerDTO.getRoleId())));

            User user = User.builder()
                    .username(registerDTO.getUsername())
                    .password(passwordEncoder.encode(registerDTO.getPassword()))
                    .name(registerDTO.getName())
                    .email(registerDTO.getEmail())
                    .isActive(true)
                    .roles(roles)
                    .build();

            userRepository.save(user);
            var token = jwtTokenProvider.generateToken(user);
            logger.info("username saved to database");
            return JwtResponse.builder().Token(token).build();
        }
        catch (Exception e ){
            logger.warn("problem occurred when saving register user to db ");
            throw e;
        }
    }
    public JwtResponse auth(LoginDTO loginDto) {

        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
            User user = userRepository.findByusername(loginDto.getUsername())
                    .orElseThrow(()-> new NullPointerException("username not found"));

            String token = jwtTokenProvider.generateToken(user);
            return JwtResponse.builder().Token(token).build();
        }
        catch (AuthenticationException e){
            logger.warn("authentication couldn't authenticate to user.");
            throw e;
        }
        catch (Exception e){
            logger.warn("user couldn't authenticated");
            throw e;
        }
    }
}

