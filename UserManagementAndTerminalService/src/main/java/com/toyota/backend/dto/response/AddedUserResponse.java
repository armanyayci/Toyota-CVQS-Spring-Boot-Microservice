package com.toyota.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Response Object for added user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddedUserResponse {
    /**
     * The name of the user for whom added.
     */
    public String name;
    /**
     * The username of the user for whom added.
     */
    public String username;
    /**
     * The email of the user for whom added.
     */
    public String email;
}
