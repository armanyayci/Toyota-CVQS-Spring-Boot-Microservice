package com.toyota.backend.service.Abstract;

import com.toyota.backend.dto.request.AddRemoveRoleDTO;
import com.toyota.backend.dto.request.RegisterDTO;
import com.toyota.backend.dto.request.UpdateUserDTO;
import com.toyota.backend.dto.response.AddedUserResponse;
import com.toyota.backend.dto.response.UpdatedUserResponse;
import com.toyota.backend.dto.response.UserViewResponse;

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
