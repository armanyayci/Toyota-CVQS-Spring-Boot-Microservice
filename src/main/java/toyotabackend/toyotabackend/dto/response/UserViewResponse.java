package toyotabackend.toyotabackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toyotabackend.toyotabackend.domain.Entity.Role;
import toyotabackend.toyotabackend.domain.Entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViewResponse {

    public String name;
    public String username;
    public String email;
    public List<String> roleList;

    public static UserViewResponse convertUserViewResponse(User user)
    {
            return UserViewResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .roleList(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .build();
    }
    public static List<UserViewResponse> convertUserListToUserViewResponse(List<User> users){

        return users.stream().map(UserViewResponse::convertUserViewResponse).collect(Collectors.toList());
    }






}
