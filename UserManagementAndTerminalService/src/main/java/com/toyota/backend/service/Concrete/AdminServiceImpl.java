package com.toyota.backend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.toyota.backend.dao.RoleRepository;
import com.toyota.backend.dao.UserRepository;
import com.toyota.backend.domain.Entity.Role;
import com.toyota.backend.domain.Entity.User;
import com.toyota.backend.dto.request.AddRemoveRoleDTO;
import com.toyota.backend.dto.request.RegisterDTO;
import com.toyota.backend.dto.request.UpdateUserDTO;
import com.toyota.backend.dto.response.AddedUserResponse;
import com.toyota.backend.dto.response.UpdatedUserResponse;
import com.toyota.backend.dto.response.UserViewResponse;
import com.toyota.backend.service.Abstract.AdminService;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class provides an implementation for the {@link AdminService} interface, handling all the business logic related to
 * admin operations on users and roles. It contains methods to authorize new users, delete and activate users, update user
 * details, add or remove roles from users, get user details and get all active users.
 * The class uses constructor injection to inject instances of {@link RoleRepository}, {@link UserRepository}, and
 * {@link PasswordEncoder} to access and manipulate data related to roles and users.
 * The class also logs information and error messages using {@link Logger} to facilitate debugging and error handling.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * Authorizes a new user to the system by creating a new user with the provided information in the {@link RegisterDTO} object.
     * @param registerDTO The DTO containing user registration information.
     * @return An instance of {@link AddedUserResponse} that contains the newly created user's details.
     * @throws NullPointerException If the role is not found with the roleId specified in the {@link RegisterDTO} object.
     * @throws UnsupportedOperationException If the operation is not supported.
     * @throws ClassCastException If there is a type casting error.
     * @throws IllegalArgumentException If there is an invalid argument.
     */
    @Override
    public AddedUserResponse AuthorizeNewUser(RegisterDTO registerDTO)
    {
        try {
            List<Role> rolelist = new ArrayList<>();
            rolelist.add(roleRepository.findById(registerDTO.getRoleId()).orElseThrow
                    (()-> new NullPointerException(String.format("Role not found with roleId: %s",registerDTO.getRoleId()))));
            User user = User.builder()
                    .username(registerDTO.getUsername())
                    .password(passwordEncoder.encode(registerDTO.getPassword()))
                    .name(registerDTO.getName())
                    .email(registerDTO.getEmail())
                    .isActive(true)
                    .roles(rolelist)
                    .build();
            userRepository.save(user);
            logger.info(String.format("user saved to database with username: %s",registerDTO.getUsername()));

            return AddedUserResponse.builder()
                    .name(registerDTO.getName())
                    .email(registerDTO.getEmail())
                    .username(registerDTO.getUsername())
                    .build();
        }
        catch (UnsupportedOperationException | ClassCastException | NullPointerException | IllegalArgumentException e){
            logger.warn(String.format("problem occurred when adding role to list, Role-Id: %s",registerDTO.getRoleId()));
            throw e;}
    }
    /**
     * Soft deletes a user with the specified id by setting the user's active status to false.
     * @param id The id of the user to be softly deleted.
     * @throws NullPointerException If the user is not found with the id specified.
     */
    @Override
    public void softDeleteUser(int id){
        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",id)));
            if (!user.isActive()){
                logger.info(String.format("user already deleted. id: %s",id));
                throw new RuntimeException();
            }

            user.setActive(false);
            userRepository.save(user);
            logger.info(String.format("user is soft deleted with id : %s", id));
        }
        catch (Exception e ){
            logger.warn(String.format("User couldn't soft deleted. id: %s",id));
            throw e;
        }
    }
    /**
     * Activates a user with the specified id by setting the user's active status to true.
     * @param id The id of the user to be activated.
     * @throws NullPointerException If the user is not found with the id specified.
     */
    @Override
    public void softActiveUser(int id) {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",id)));
            if (user.isActive()){
                logger.info(String.format("user already active. id: %s",id));
                throw new RuntimeException();
            }
            user.setActive(true);
            userRepository.save(user);
            logger.info(String.format("user is activated with id: %s ",id));
        }
        catch (Exception e){
            logger.warn(String.format("User couldn't activated. id: %s",id));
            throw e;
        }
    }
    /**
     * Updates a user's details with the provided information in the {@link UpdateUserDTO} object.
     * @param dto The DTO containing the updated user information.
     * @param id The id of the user to be updated.
     * @return An instance of {@link UpdatedUserResponse} that contains the updated user's details.
     * @throws NullPointerException If the user is not found with the id specified.
     */
    @Override
    public UpdatedUserResponse updateUser(UpdateUserDTO dto, int id) {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",id)));
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setUsername(dto.getUsername());
            userRepository.save(user);
            logger.info(String.format("user updated user-id: %s ",id));

            return UpdatedUserResponse.convert(user);
        }
        catch (Exception e){
            logger.warn(String.format("User couldn't updated. id: %s",id));
            throw e;
        }
    }
    /**
     * Adds a role to a user with the provided information in the {@link AddRemoveRoleDTO} object.
     * @param dto The DTO containing the user and role information.
     * @throws NullPointerException If the user or the role is not found with the specified information.
     * @throws UnsupportedOperationException If the operation is not supported.
     * @throws ClassCastException If there is a type casting error.
     * @throws IllegalArgumentException If there is an invalid argument.
     */
    @Override
    public void addRole(AddRemoveRoleDTO dto) {
        try {
            User user = userRepository.findByusername(dto.getUsername()).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s", dto.getUsername())));
            Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                    ()-> new NullPointerException(String.format("Role not found with role id: %s", dto.getRoleId())));
            List<Role> roles = user.getRoles();
            if (roles.contains(role)){
                logger.debug(String.format("user already has role with id : %s ",dto.getRoleId()));
                throw new RuntimeException();
            }
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
            logger.info(String.format("role(id:%s) added to user(username: %s) ",dto.getRoleId(),dto.getUsername()));
        }
        catch (UnsupportedOperationException | ClassCastException | NullPointerException | IllegalArgumentException e){
            logger.warn("error occurred while adding role to list.");
            throw e;
        }
    }

/**
     * Removes a role from the user with provided information in the {@link AddRemoveRoleDTO} object.
     * @param dto the DTO containing the username and roleId of the user.
     * @throws NullPointerException If the user or the role is not found with the specified information.
     * @throws UnsupportedOperationException If the operation is not supported.
     * @throws ClassCastException If there is a type casting error.
     * @throws IllegalArgumentException If there is an invalid argument.
 */
 @Override
    public void removeRole(AddRemoveRoleDTO dto) {
        try {
            User user = userRepository.findByusername(dto.getUsername()).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s " , dto.getUsername())));

            Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                    ()-> new NullPointerException(String.format("Role not found wih id: %s ", dto.getRoleId())));

            List<Role> roles = user.getRoles();

            if (!roles.contains(role)){
                logger.debug(String.format("User has no role with that id: %s ", dto.getRoleId()));
                throw new NullPointerException(String.format("user has no role with that id %s ", dto.getRoleId()));
            }
            roles.remove(role);
            user.setRoles(roles);
            userRepository.save(user);
            logger.info(String.format("role(id:%s) removed from the user(username: %s) ",dto.getRoleId(),dto.getUsername()));
        }
            catch (UnsupportedOperationException | ClassCastException | NullPointerException e){
            logger.warn("when removing role from the list it errored.");
            throw e;
        }
    }
    /**
     * Retrieves a list of active users and returns them as UserViewResponse objects.
     * @return a list of UserViewResponse objects representing the active users in the system
     */
    @Override
    public List<UserViewResponse> getUsers() {
            List<User> users = userRepository.getActiveUsers();
            return UserViewResponse.convertUserListToUserViewResponse(users);
    }
    /**
     * Retrieves the user with the given ID and returns it as a UserViewResponse object.
     * @param id the ID of the user to retrieve
     * @return a UserViewResponse object representing the retrieved user
     * @throws NullPointerException if the user is not found
     */
    @Override
    public UserViewResponse getUser(int id) {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with id: %s ", id)));
            return UserViewResponse.convertUserViewResponse(user);
    }
}