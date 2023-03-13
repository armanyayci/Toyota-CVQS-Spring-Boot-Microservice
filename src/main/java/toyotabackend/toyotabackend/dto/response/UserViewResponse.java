package toyotabackend.toyotabackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViewResponse {

    public String name;
    public String username;
    public String email;
    public List<String> roleList;

}
