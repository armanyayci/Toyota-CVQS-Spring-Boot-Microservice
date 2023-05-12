package com.toyota.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * DTO class for registering a new user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    /**
     * The name of the user.
     */
    @Size(min = 4, max = 30,message = "name can not be less than 4 and more than 32")
    @NotEmpty
    private String name;
    /**
     * The username of the user.
     */
    @Size(min = 4, max = 50,message = "username can not be less than 4 and more than 32")
    @NotEmpty
    private String username;
    /**
     * The email of the user.
     */
    @Size(min = 4, max = 50,message = "email can not be less than 4 and more than 32")
    @NotEmpty
    private String email;
    /**
     * The password of the user.
     */
    @Size(min = 4, max = 100,message = "password can not be less than 4 and more than 32")
    private String password;
    /**
     * The role ID of the user.
     */
    @NotNull
    private int roleId;

}
