package toyotabackend.toyotabackend.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @Size(min = 4, max = 50,message = "username can not be less than 4 and more than 32")
    @NotEmpty
    public String username;
    @Size(min = 4, max = 100,message = "password can not be less than 4 and more than 32")
    @NotEmpty
    public String password;
}
