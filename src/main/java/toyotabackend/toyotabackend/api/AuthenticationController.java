package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyotabackend.toyotabackend.dto.LoginDTO;
import toyotabackend.toyotabackend.dto.RegisterDTO;
import toyotabackend.toyotabackend.dto.JwtResponse;
import toyotabackend.toyotabackend.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = Logger.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register (@RequestBody RegisterDTO registerDTO)
    {
        return ResponseEntity.ok(authenticationService.save(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@RequestBody LoginDTO loginDTO){

        return ResponseEntity.ok(authenticationService.auth(loginDTO));
    }
}

