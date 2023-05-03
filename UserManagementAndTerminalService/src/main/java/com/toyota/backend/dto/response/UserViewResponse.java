package com.toyota.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.toyota.backend.domain.Entity.Role;
import com.toyota.backend.domain.Entity.User;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Response Object for User View.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViewResponse {
    /**
     * The name of the user.
     */
    public String name;
    /**
     * The username of the user.
     */
    public String username;
    /**
     * The email of the user.
     */
    public String email;
    /**
     * The list of the roles.
     */
    public List<String> roleList;
    /**
     * Converts a User object into a UserViewResponse object.
     * @param user the User object to be converted.
     * @return a UserViewResponse object containing the relevant information from the User object.
     */
    public static UserViewResponse convertUserViewResponse(User user)
    {
            return UserViewResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .roleList(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .build();
    }
    /**
     * Converts a list of User objects into a list of UserViewResponse objects.
     * @param users the list of User objects to be converted.
     * @return a list of UserViewResponse objects containing the relevant information from each User object in the input list.
     */
    public static List<UserViewResponse> convertUserListToUserViewResponse(List<User> users){
        return users.stream().map(UserViewResponse::convertUserViewResponse).collect(Collectors.toList());
    }

}
