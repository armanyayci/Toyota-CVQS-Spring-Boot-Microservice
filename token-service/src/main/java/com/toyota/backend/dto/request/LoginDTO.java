package com.toyota.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) class representing login information for a user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    /**
     * The username of the user attempting to log in.
     */
    @Size(min = 4, max = 50,message = "username can not be less than 4 and more than 32")
    @NotEmpty
    public String username;
    /**
     * The password of the user attempting to log in.
     */
    @Size(min = 4, max = 100,message = "password can not be less than 4 and more than 32")
    @NotEmpty
    public String password;
}
