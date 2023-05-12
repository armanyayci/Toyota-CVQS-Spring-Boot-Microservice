package com.toyota.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.toyota.backend.domain.Entity.User;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Response Object for Updated User.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedUserResponse {
    /**
     * The name of the updated user.
     */
    private String name;
    /**
     * The username of the updated user.
     */
    private String username;
    /**
     * The email of the updated user.
     */
    private String email;

    /**
     * Converts a User entity to an UpdatedUserResponse DTO object.
     * @param user the User entity to convert.
     * @return an UpdatedUserResponse DTO object representing the converted User entity.
     */
    public static UpdatedUserResponse convert(User user){
        return new UpdatedUserResponse(user.getName(), user.getUsername(), user.getEmail());
    }
}
