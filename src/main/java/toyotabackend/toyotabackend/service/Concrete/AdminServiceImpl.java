package toyotabackend.toyotabackend.service.Concrete;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toyotabackend.toyotabackend.dao.RoleRepository;
import toyotabackend.toyotabackend.dao.UserRepository;
import toyotabackend.toyotabackend.domain.Role;
import toyotabackend.toyotabackend.domain.User;
import toyotabackend.toyotabackend.dto.request.AddRemoveRoleDTO;
import toyotabackend.toyotabackend.dto.request.RegisterDTO;
import toyotabackend.toyotabackend.dto.request.UpdateUserDTO;
import toyotabackend.toyotabackend.dto.response.UserViewResponse;
import toyotabackend.toyotabackend.dto.response.AddedUserResponse;
import toyotabackend.toyotabackend.dto.response.UpdatedUserResponse;
import toyotabackend.toyotabackend.security.JwtTokenProvider;
import toyotabackend.toyotabackend.service.Abstract.AdminService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AddedUserResponse adminAdded(RegisterDTO registerDTO)
    {
        try {
            List<Role> rolelist = new ArrayList<>();

            rolelist.add(roleRepository.findById(registerDTO.getRoleId()).orElseThrow
                    (()-> new EntityNotFoundException("Role is not found with id -> "+ registerDTO.getRoleId())));

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
        catch (Exception e ){
            logger.warn("problem occurred when saving register user to db ");
            throw e;
        }
    }
    @Override
    public void softDeleteUser(int id){
        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new EntityNotFoundException("User not found with username -> " + id));
            user.setActive(false);
            userRepository.save(user);
            logger.info("user is soft deleted with id -> "+ id);
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
                    ()-> new EntityNotFoundException("User not found with username -> "+ id));
            user.setActive(true);
            userRepository.save(user);
            logger.info("user is activated with id -> "+ id);

        }
        catch (Exception e){
            logger.warn("Soft update operation errored in service.");
            throw e;
        }
    }
    @Override
    public UpdatedUserResponse updateUser(UpdateUserDTO dto, int id) {

        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new UsernameNotFoundException("User not found with username -> " + dto.getUsername()));

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
                    ()-> new EntityNotFoundException("User not found with username -> " + dto.getUsername()));

            Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                    ()-> new EntityNotFoundException("Role is not found with id -> "+ dto.getRoleId()));


            List<Role> roles = user.getRoles();

            if (roles.contains(role)){
                logger.debug("user already has role with id -> " + dto.getRoleId());
                throw new IllegalArgumentException("User already has that role");
            }

            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
        catch (Exception e) {
            logger.warn("add role operation errored in service.");
            throw e;
        }
    }
    @Override
    public void removeRole(AddRemoveRoleDTO dto) {
        try {
            User user = userRepository.findByusername(dto.getUsername()).orElseThrow(
                    ()-> new EntityNotFoundException("User not found with username -> " + dto.getUsername()));

            Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                    ()-> new EntityNotFoundException("Role not found wih id -> " + dto.getRoleId()));

            List<Role> roles = user.getRoles();

            if (!roles.contains(role)){
                logger.debug("User has no role with that id -> "+ dto.getRoleId());
                throw new IllegalArgumentException("user has no role with that id -> "+ dto.getRoleId());
            }
            roles.remove(role);
            userRepository.save(user);
        }
        catch (Exception e){
            logger.warn("remove role operation errored in service.");
            throw e;
        }
    }
    @Override
    public List<UserViewResponse> getUsers() {
        try {
            List<User> users = userRepository.getActiveUsers();
            List<UserViewResponse> dtos = new ArrayList<>();
            for(User user : users) {
                UserViewResponse dto = new UserViewResponse();
                dto.name = user.getName();
                dto.email = user.getEmail();
                dto.username = user.getUsername();
                dto.roleList = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

                dtos.add(dto);
            }
            return dtos;
        }
        catch (Exception e){
            logger.warn("get users operation errored in service.");
            throw e;
        }
    }
    @Override
    public UserViewResponse getUser(int id) {

        try {
            User user = userRepository.findById(id).orElseThrow(
                    ()-> new EntityNotFoundException("User not found with id -> " + id));

            return UserViewResponse.builder()
                    .username(user.getUsername())
                    .name(user.getName())
                    .email(user.getEmail())
                    .roleList(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                    .build();
        }

        catch (Exception e){
            logger.warn("get user operation errored in service.");
            throw e;
        }

    }
}










