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
    void softDeleteUser(String username);
    void softActiveUser(String username);
    UpdatedUserResponse updateUser(UpdateUserDTO dto, String username);
    void addRole(AddRemoveRoleDTO dto);
    void removeRole(AddRemoveRoleDTO dto);

    List<UserViewResponse> getActiveUsers(int page, int size, String sortBy, String filter);

    UserViewResponse getUser(String username);

    List<UserViewResponse> getAllUsers(int page, int size, String sortBy, String filter);
}
