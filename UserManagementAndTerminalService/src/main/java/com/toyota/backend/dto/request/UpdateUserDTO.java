package com.toyota.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Data Transfer Object for update user credentials.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    /**
     * The name of the user for whom the credential will be updated.
     */
    @Size(min = 4, max = 30,message = "name can not be less than 4 and more than 32")
    @NotEmpty
    private String name;
    /**
     * The username of the user for whom the credential will be updated.
     */
    @Size(min = 4, max = 50,message = "username can not be less than 4 and more than 32")
    @NotEmpty
    private String username;
    /**
     * The email of the user for whom the credential will be updated.
     */
    @Size(min = 4, max = 50,message = "email can not be less than 4 and more than 32")
    @NotEmpty
    private String email;
    /**
     * The password of the user for whom credential will be updated.
     */
    @Size(min = 4, max = 100,message = "password can not be less than 4 and more than 32")
    @NotEmpty
    private String password;

}
