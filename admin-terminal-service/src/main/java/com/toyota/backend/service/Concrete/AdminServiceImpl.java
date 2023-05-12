package com.toyota.backend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.stream.Collectors;

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

            if (userRepository.existsByusername(registerDTO.getUsername()) || userRepository.existsByemail(registerDTO.getEmail())){
                logger.info("the user is already registered with given username or email.");
                throw new RuntimeException();
            }
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
     * @param username The username of the user to be softly deleted.
     * @throws NullPointerException If the user is not found with the id specified.
     */
    @Override
    public void softDeleteUser(String username){
        try {
            User user = userRepository.findByusername(username).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",username)));
            if (!user.isActive()){
                logger.info(String.format("user already deleted. id: %s",username));
                throw new RuntimeException();
            }

            user.setActive(false);
            userRepository.save(user);
            logger.info(String.format("user: %s is soft deleted", username));
        }
        catch (Exception e ){
            logger.warn(String.format("User couldn't soft deleted. username: %s",username));
            throw e;
        }
    }
    /**
     * Activates a user with the specified id by setting the user's active status to true.
     * @param username The username of the user to be activated.
     * @throws NullPointerException If the user is not found with the id specified.
     */
    @Override
    public void softActiveUser(String username) {
        try {
            User user = userRepository.findByusername(username).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",username)));
            if (user.isActive()){
                logger.info(String.format("user is already active. username: %s",username));
                throw new RuntimeException();
            }
            user.setActive(true);
            userRepository.save(user);
            logger.info(String.format("user: %s is activated",username));
        }
        catch (Exception e){
            logger.warn(String.format("User: %s couldn't activated.",username));
            throw e;
        }
    }
    /**
     * Updates a user's details with the provided information in the {@link UpdateUserDTO} object.
     * @param dto The DTO containing the updated user information.
     * @param username The username of the user to be updated.
     * @return An instance of {@link UpdatedUserResponse} that contains the updated user's details.
     * @throws NullPointerException If the user is not found with the id specified.
     */
    @Override
    public UpdatedUserResponse updateUser(UpdateUserDTO dto, String username) {
        try {

            User user = userRepository.findByusername(username).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",username)));

            if (user.getUsername().equals(dto.getUsername()) || user.getEmail().equals(dto.getEmail())){

                if (userRepository.existsByusername(dto.getUsername()) || userRepository.existsByemail(dto.getEmail())){
                    logger.info(String.format("username(%s) or email(%s) that want to update is already registered in the db.",dto.getUsername(),dto.getEmail()));
                    throw new RuntimeException();
                }

            }
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setUsername(dto.getUsername());
            userRepository.save(user);
            logger.info(String.format("user informations are updated. new username: %s ",dto.getUsername()));

            return UpdatedUserResponse.convert(user);
        }
        catch (Exception e){
            logger.warn(String.format("User couldn't updated. username: %s",username));
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
            else if (roles.size()==1){
                logger.info(String.format("user: %s has only 1 role. remove operation can not be performed.",user.getUsername()));
                throw new RuntimeException();
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
     * @param page The page number to return. Defaults to 0.
     * @param size The number of items per page. Defaults to 10.
     * @param sortBy The field to sort the results by. Defaults to "id".
     * @param filter that field filter results as name. Default brings all.
     * @return a list of UserViewResponse objects representing the active users in the system
     */
    @Override
    public List<UserViewResponse> getActiveUsers(int page, int size, String sortBy, String filter) {

        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<User> userList;

        if (filter.isEmpty()){
            userList = userRepository.getActiveUsers(paging);
            return UserViewResponse.convertUserListToUserViewResponse(userList);}

        userList = userRepository.findActiveUserByFilter(filter,paging);
        return UserViewResponse.convertUserListToUserViewResponse(userList);
    }
    /**
     * Retrieves the user with the given ID and returns it as a UserViewResponse object.
     * @param page The page number to return. Defaults to 0.
     * @param size The number of items per page. Defaults to 10.
     * @param sortBy The field to sort the results by. Defaults to "id".
     * @param filter that field filter results as name. Default brings all.
     * @return a UserViewResponse object representing the retrieved user
     * @throws NullPointerException if the user is not found
     */
    @Override
    public List<UserViewResponse> getAllUsers(int page, int size, String sortBy, String filter) {

        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        Page<User> userList;

        if (filter.isEmpty())
            userList = userRepository.findAll(paging);
        else
            userList = userRepository.findUserByFilter(filter,paging);

        return UserViewResponse.convertUserListToUserViewResponse(userList);
    }

    /**
     * Retrieves the user with the given ID and returns it as a UserViewResponse object.
     * @param username the username of the user to retrieve
     * @return a UserViewResponse object representing the retrieved user
     * @throws NullPointerException if the user is not found
     */
    @Override
    public UserViewResponse getUser(String username) {
        User user = userRepository.findByusername(username).orElseThrow(
                ()-> new NullPointerException(String.format("User not found with id: %s ", username)));
        return UserViewResponse.convertUserViewResponse(user);
    }
}





















