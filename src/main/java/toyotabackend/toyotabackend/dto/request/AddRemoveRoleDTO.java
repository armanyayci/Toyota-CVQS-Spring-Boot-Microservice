package toyotabackend.toyotabackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRemoveRoleDTO {

    @NotEmpty
    @Size(min = 4, max = 50,message = "username can not be less than 4 and more than 32")
    private String username;
    @NotNull
    private Integer roleId;

}
