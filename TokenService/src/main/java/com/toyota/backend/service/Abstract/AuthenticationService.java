package com.toyota.backend.service.Abstract;

import com.toyota.backend.dto.request.LoginDTO;
import com.toyota.backend.dto.response.JwtResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PathVariable;

public interface AuthenticationService {

    JwtResponse auth(LoginDTO loginDto) throws AuthenticationException;
    boolean isValid (String token);

}
