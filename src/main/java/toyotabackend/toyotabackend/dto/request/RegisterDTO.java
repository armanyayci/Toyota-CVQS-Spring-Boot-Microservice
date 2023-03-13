package toyotabackend.toyotabackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import toyotabackend.toyotabackend.domain.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @Size(min = 4, max = 30,message = "name can not be less than 4 and more than 32")
    @NotEmpty
    private String name;
    @Size(min = 4, max = 50,message = "username can not be less than 4 and more than 32")
    @NotEmpty
    private String username;
    @Size(min = 4, max = 50,message = "email can not be less than 4 and more than 32")
    @NotEmpty
    private String email;
    @Size(min = 4, max = 50,message = "password can not be less than 4 and more than 32")
    @NotEmpty
    private String password;
    @NotNull
    private int roleId;

}
