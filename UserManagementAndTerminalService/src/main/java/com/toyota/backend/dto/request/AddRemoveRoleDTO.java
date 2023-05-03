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
 * Data Transfer Object for adding or removing role from a user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRemoveRoleDTO {
    /**
     * The username of the user for whom the role will be added or removed.
     */
    @NotEmpty
    @Size(min = 4, max = 50,message = "username can not be less than 4 and more than 32")
    private String username;
    /**
     * The id of the role which will be added or removed from the user.
     */
    @NotNull
    private Integer roleId;

}
