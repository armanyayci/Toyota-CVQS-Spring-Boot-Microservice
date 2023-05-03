package com.toyota.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.toyota.backend.dto.request.AddRemoveRoleDTO;
import com.toyota.backend.dto.request.RegisterDTO;
import com.toyota.backend.dto.request.UpdateUserDTO;
import com.toyota.backend.dto.response.AddedUserResponse;
import com.toyota.backend.dto.response.UpdatedUserResponse;
import com.toyota.backend.dto.response.UserViewResponse;
import com.toyota.backend.service.Concrete.AdminServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class represents the REST API endpoints for the admin functionalities.
 * It provides CRUD operations for users and their roles.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;
    /**
     * Adds a new user with the given information.
     * @param registerDTO the user's registration information
     * @return the HTTP response containing the added user's information
     */
    @PostMapping("/add")
    public ResponseEntity<AddedUserResponse> add(@Valid @RequestBody RegisterDTO registerDTO){

        return ResponseEntity.ok(adminServiceImpl.AuthorizeNewUser(registerDTO));
    }
    /**
     * Soft deletes a user with the given id.
     * @param id the id of the user to be deleted
     * @return the HTTP response indicating the successful deletion
     */
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteUser(@Valid @PathVariable("id") int id) {
        adminServiceImpl.softDeleteUser(id);
        return ResponseEntity.ok("User is soft deleted");
    }
    /**
     * Activates a soft-deleted user with the given id.
     * @param id the id of the user to be activated
     * @return the HTTP response indicating the successful activation
     */
    @GetMapping("/active/{id}")
    public ResponseEntity<String> softActiveUser(@Valid @PathVariable("id") int id) {
        adminServiceImpl.softActiveUser(id);
        return ResponseEntity.ok("User is activated");
    }
    /**
     * Updates an existing user's information with the given data.
     * @param id the id of the user to be updated
     * @param dto the user's updated information
     *@return the HTTP response containing the updated user's information
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdatedUserResponse> updateUser (@Valid @PathVariable("id") int id, @RequestBody UpdateUserDTO dto) {

        return ResponseEntity.ok(adminServiceImpl.updateUser(dto,id));
    }
    /**
     * Adds a role to an existing user with the given information.
     * @param dto the information of the role to be added
     * @return the HTTP response indicating the successful addition of the role
     */
    @PostMapping("/addrole")
    public ResponseEntity<String> addRole(@Valid @RequestBody AddRemoveRoleDTO dto){

        adminServiceImpl.addRole(dto);
        return ResponseEntity.ok(String.format("User role is added with id: %s ", dto.getRoleId()));
    }
    /**
     * Removes a role from an existing user with the given information.
     * @param dto the information of the role to be removed
     * @return the HTTP response indicating the successful removal of the role
     */
    @PostMapping("/removerole")
    public ResponseEntity<String> removeRole(@Valid @RequestBody AddRemoveRoleDTO dto){

        adminServiceImpl.removeRole(dto);
        return ResponseEntity.ok(String.format("User role is deleted with id: %s ", dto.getRoleId()));
    }
    /**
     * Retrieves a list of all existing users.
     * @return the HTTP response containing a list of all users' information
     */
    @GetMapping("/getusers")
    public ResponseEntity<List<UserViewResponse>>getUsers(){

        return ResponseEntity.ok(adminServiceImpl.getUsers());
    }
    /**
     Retrieves the information of an existing user with the given id.
     @param id the id of the user to retrieve
     @return the HTTP response containing the user's information
     */
    @GetMapping("/getuser/{id}")
    public ResponseEntity<UserViewResponse> getUser(@PathVariable("id") int id ){
        return ResponseEntity.ok(adminServiceImpl.getUser(id));
    }
}
