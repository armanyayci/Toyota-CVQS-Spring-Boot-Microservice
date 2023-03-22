package toyotabackend.toyotabackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toyotabackend.toyotabackend.domain.Entity.User;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedUserResponse {
    private String name;
    private String username;
    private String email;

    public static UpdatedUserResponse convert(User user){

        return new UpdatedUserResponse(user.getName(), user.getUsername(), user.getEmail());
    }


}
