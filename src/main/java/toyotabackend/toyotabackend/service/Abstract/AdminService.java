package toyotabackend.toyotabackend.service.Abstract;

import toyotabackend.toyotabackend.dto.request.AddRemoveRoleDTO;
import toyotabackend.toyotabackend.dto.request.RegisterDTO;
import toyotabackend.toyotabackend.dto.request.UpdateUserDTO;
import toyotabackend.toyotabackend.dto.response.UserViewResponse;
import toyotabackend.toyotabackend.dto.response.AddedUserResponse;
import toyotabackend.toyotabackend.dto.response.UpdatedUserResponse;

import java.util.List;

public interface AdminService {

    AddedUserResponse AuthorizeNewUser(RegisterDTO registerDTO);
    void softDeleteUser(int id);
    void softActiveUser(int id);
    UpdatedUserResponse updateUser(UpdateUserDTO dto, int id);
    void addRole(AddRemoveRoleDTO dto);
    void removeRole(AddRemoveRoleDTO dto);

    List<UserViewResponse> getUsers();

    UserViewResponse getUser(int id);
}
