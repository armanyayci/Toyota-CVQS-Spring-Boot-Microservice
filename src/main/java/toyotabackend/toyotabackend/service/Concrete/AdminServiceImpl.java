package toyotabackend.toyotabackend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toyotabackend.toyotabackend.dao.RoleRepository;
import toyotabackend.toyotabackend.dao.UserRepository;
import toyotabackend.toyotabackend.domain.Entity.Role;
import toyotabackend.toyotabackend.domain.Entity.User;
import toyotabackend.toyotabackend.dto.request.AddRemoveRoleDTO;
import toyotabackend.toyotabackend.dto.request.RegisterDTO;
import toyotabackend.toyotabackend.dto.request.UpdateUserDTO;
import toyotabackend.toyotabackend.dto.response.UserViewResponse;
import toyotabackend.toyotabackend.dto.response.AddedUserResponse;
import toyotabackend.toyotabackend.dto.response.UpdatedUserResponse;
import toyotabackend.toyotabackend.security.JwtTokenProvider;
import toyotabackend.toyotabackend.service.Abstract.AdminService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AddedUserResponse AuthorizeNewUser(RegisterDTO registerDTO)
    {
        try {
            List<Role> rolelist = new ArrayList<>();

            rolelist.add(roleRepository.findById(registerDTO.getRoleId()).orElseThrow
                    (()-> new NullPointerException(String.format("User not found with username: %s",registerDTO.getRoleId()))));

            User user = User.builder()
                    .username(registerDTO.getUsername())
                    .password(passwordEncoder.encode(registerDTO.getPassword()))
                    .name(registerDTO.getName())
                    .email(registerDTO.getEmail())
                    .isActive(true)
                    .roles(rolelist)
                    .build();
            userRepository.save(user);

            var token = jwtTokenProvider.generateToken(user);
            logger.info("user saved to database");
            return AddedUserResponse.builder()
                    .name(registerDTO.getName())
                    .email(registerDTO.getEmail())
                    .username(registerDTO.getUsername())
                    .token(token).build();
        }
        catch (UnsupportedOperationException | ClassCastException | NullPointerException | IllegalArgumentException e){
            logger.warn("problem occurred when adding role to list");
            throw e;}
    }
    @Override
    public void softDeleteUser(int id){
        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",id)));
            user.setActive(false);
            userRepository.save(user);
            logger.info(String.format("user is soft deleted with id : %s", id));
        }
        catch (Exception e ){
            logger.warn("user soft delete operation errored in service.");
            throw e;
        }
    }

    @Override
    public void softActiveUser(int id) {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s",id)));
            user.setActive(true);
            userRepository.save(user);
            logger.info(String.format("user is activated with id: %s ",id));
        }
        catch (Exception e){
            logger.warn("error occurred when activating user.");
            throw e;
        }
    }
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

            return UpdatedUserResponse.convert(user);
        }
        catch (Exception e){
            logger.warn("update user operation has crushed in service.");
            throw e;
        }
    }
    @Override
    public void addRole(AddRemoveRoleDTO dto) {

        try {
            User user = userRepository.findByusername(dto.getUsername()).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with username: %s", dto.getUsername())));

            Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                    ()-> new NullPointerException(String.format("Role not found with role id: %s", dto.getRoleId())));


            List<Role> roles = user.getRoles();

            if (roles == null) {
                roles = new ArrayList<>();
                user.setRoles(roles);
            }
            else if (roles.contains(role)){
                logger.debug(String.format("user already has role with id : %s ",dto.getRoleId()));
                throw new ClassCastException();
            }

            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
        catch (UnsupportedOperationException | ClassCastException | NullPointerException | IllegalArgumentException e){
            logger.warn("error occurred while adding role to list.");
            throw e;
        }
    }
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
            userRepository.save(user);}
        catch (UnsupportedOperationException | ClassCastException | NullPointerException e){
            logger.warn("when removing role from the list it throws error.");
            throw e;
        }
    }
    @Override
    public List<UserViewResponse> getUsers() {
            List<User> users = userRepository.getActiveUsers();
            return UserViewResponse.convertUserListToUserViewResponse(users);
        }
    @Override
    public UserViewResponse getUser(int id) {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("User not found with id: %s ", id)));
            return UserViewResponse.convertUserViewResponse(user);
    }
}