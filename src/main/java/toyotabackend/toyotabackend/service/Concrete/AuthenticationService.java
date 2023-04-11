package toyotabackend.toyotabackend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import toyotabackend.toyotabackend.dao.UserRepository;
import toyotabackend.toyotabackend.domain.Entity.User;
import toyotabackend.toyotabackend.dto.request.LoginDTO;
import toyotabackend.toyotabackend.dto.response.JwtResponse;
import toyotabackend.toyotabackend.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = Logger.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponse auth(LoginDTO loginDto) {

        try {
            User user = userRepository.findByusername(loginDto.getUsername())
                    .orElseThrow(()-> new NullPointerException(String.format("username not found : %s",loginDto.getUsername())));
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));

            String token = jwtTokenProvider.generateToken(user);
            return JwtResponse.builder().Token(token).build();
        }
        catch (AuthenticationException e){
            logger.warn("Password is not correct.");
            throw e;
        }
    }
}

