package com.toyota.backend.service.Concrete;

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
import com.toyota.backend.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link AdminServiceImpl} class.
 */
@SpringBootTest
class AdminServiceImplTest extends TestUtils {


    private AdminServiceImpl adminService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        adminService = new AdminServiceImpl(roleRepository,userRepository,passwordEncoder);
    }

    @Test
    public void AuthorizeNewUser_WhenCalledWithValidRegisterDTO_itShouldReturnValidAddedUserDTO()
    {
        RegisterDTO dto = generateRegisterDto();
        Role role = generateRole();

        when(roleRepository.findById(any(Integer.class))).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPasswordTest");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        AddedUserResponse response = adminService.AuthorizeNewUser(dto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(dto.getName());
        assertThat(response.getEmail()).isEqualTo(dto.getEmail());
        assertThat(response.getUsername()).isEqualTo(dto.getUsername());
    }

    @Test
    public void AuthorizeNewUser_whenCalledWithNotExistRole_itShouldThrowNullPointerException()
    {
        RegisterDTO registerDTO = generateRegisterDto();

        when(roleRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> adminService.AuthorizeNewUser(registerDTO));
    }

    @Test
    public void softDeleteUser_whenCalledWithIdParameter_itShouldSuccess()
    {
        User user = generateUser();

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        adminService.softDeleteUser(user.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
        assertFalse(user.isActive());
    }

    @Test
    public void softDeleteUser_whenCalledWithNotExistUser_itShouldThrowNullPointerException()
    {
        User user = generateUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        verify(userRepository,times(0)).save(user);
        assertThrows(NullPointerException.class, () -> adminService.softDeleteUser(user.getId()));
    }
    @Test
    public void softDeleteUser_whenCalledWithAlreadyDeletedUserId_itShouldThrowRuntimeException()
    {
        User user = generateUser();
        user.setActive(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> adminService.softDeleteUser(user.getId()));
        verify(userRepository,times(0)).save(user);}
    @Test
    public void softActiveUser_whenCalledWithIdParameter_itShouldSuccess()
    {
        User user = generateUser();
        user.setActive(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        adminService.softActiveUser(user.getId());
        verify(userRepository,times(1)).findById(user.getId());
        verify(userRepository,times(1)).save(user);
        assertTrue(user.isActive());

    }
    @Test
    public void softActiveUser_whenCalledWithNotExistUser_itShouldThrowNullPointerException(){

        User user = generateUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        verify(userRepository,times(0)).save(user);
        assertThrows(NullPointerException.class, ()-> adminService.softActiveUser(user.getId()));
    }

    @Test
    public void softActiveUser_whenCalledWithAlreadyActiveUserId_itShouldThrowRuntimeException(){
        User user = generateUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, ()-> adminService.softActiveUser(user.getId()));
        verify(userRepository,times(0)).save(user);
    }


    @Test
    public void updateUser_whenCalledWithValidUpdateUserDTOAndIdParameter_itShouldReturnValidUpdatedUserResponse(){

        int userId = 1;
        User user = generateUser();
        UpdateUserDTO updateUserDTO = generateUpdateUserDto();

        user.setName(updateUserDTO.getName());
        user.setEmail(updateUserDTO.getEmail());
        user.setUsername(updateUserDTO.getUsername());
        user.setPassword(updateUserDTO.getPassword());

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UpdatedUserResponse result = adminService.updateUser(updateUserDTO,userId);
        assertEquals(UpdatedUserResponse.convert(user), result);
    }

    @Test
    public void updateUser_whenCalledWithNotExistUser_itShouldThrowNullPointerException(){

        User user = generateUser();
        UpdateUserDTO dto = generateUpdateUserDto();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        verify(userRepository,times(0)).save(user);
        assertThrows(NullPointerException.class, ()-> adminService.updateUser(dto,user.getId()));
    }

    @Test
    public void addRole_whenCalledWithValidAddRemoveRoleDTO_itShouldSuccess(){

        User user = generateUser();
        Role role = generateRole();
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();

        // set up mockito behavior
        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findById(dto.getRoleId())).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);

        // perform the method
        adminService.addRole(dto);

        // verify the result
        verify(userRepository, times(1)).findByusername(dto.getUsername());
        verify(roleRepository, times(1)).findById(dto.getRoleId());
        verify(userRepository, times(1)).save(user);

        assertNotNull(user.getRoles());
        assertEquals(role.getId(), user.getRoles().get(0).getId());
    }

    @Test
    public void addRole_whenCalledWithUserOwnedRole_itShouldThrowClassCastException()
    {
        User user = generateUser();
        Role role = generateRole();
        user.setRoles(List.of(role));
        role.setUsers(List.of(user));
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();

        List<Role> roles = user.getRoles();
        user.setRoles(roles);

        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findById(dto.getRoleId())).thenReturn(Optional.of(role));

        assertThrows(RuntimeException.class, ()-> adminService.addRole(dto));

        verify(userRepository,times(0)).save(user);
        assertTrue(roles.contains(role));
    }

    @Test
    public void addRole_whenCalledWithNotExistUser_itShouldThrowNullPointerException(){

        User user = generateUser();
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();
        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, ()-> adminService.addRole(dto));
        verify(userRepository,times(0)).save(user);
    }

    @Test
    public void addRole_whenCalledWithNotExistRole_itShouldThrowNullPointerException(){

        User user = generateUser();
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();
        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findById(dto.getRoleId())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, ()-> adminService.addRole(dto));
        verify(userRepository,times(0)).save(user);
    }
    @Test
    public void removeRole_whenCalledWithValidAddRemoveRoleDTO_itShouldSuccess() {

        User user = generateUser();
        Role role = generateRole();
        user.getRoles().add(role);
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findById(dto.getRoleId())).thenReturn(Optional.of(role));

        assertEquals(user.getRoles(),roles);
        adminService.removeRole(dto);

        assertTrue(roles.contains(role));
        verify(userRepository,times(1)).save(user);
        verify(userRepository, times(1)).findByusername(dto.getUsername());
        verify(roleRepository, times(1)).findById(dto.getRoleId());
    }

    @Test
    public void removeRole_whenCalledWithNotExistUser_itShouldThrowNullPointerException(){

        User user = generateUser();
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();
        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, ()-> adminService.removeRole(dto));
        verify(userRepository,times(0)).save(user);
    }
    @Test
    public void removeRole_whenCalledWithNotExistRole_itShouldThrowNullPointerException(){

        User user = generateUser();
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();

        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findById(dto.getRoleId())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, ()-> adminService.removeRole(dto));
        verify(userRepository,times(0)).save(user);
    }

    @Test
    public void removeRole_whenCalledWithUserDoesNotHaveDesiredRole_itShouldThrowNullPointerException(){

        User user = generateUser();
        Role role = generateRole();
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();
        List<Role> roles = user.getRoles();

        when(userRepository.findByusername(dto.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findById(dto.getRoleId())).thenReturn(Optional.of(role));

        assertThrows(NullPointerException.class, ()-> adminService.removeRole(dto));

        assertFalse(roles.contains(role));
        verify(userRepository,times(0)).save(user);
    }

    @Test
    public void getUsers_whenCalled_itShouldReturnListOfUserViewResponse(){

        List<User> users = generateUserList();
        when(userRepository.getActiveUsers()).thenReturn(users);

        List<UserViewResponse> result = adminService.getUsers();
        List<UserViewResponse> response = UserViewResponse.convertUserListToUserViewResponse(users);
        assertEquals(result,response);
        verify(userRepository,times(1)).getActiveUsers();
    }

    @Test
    public void getUser_whenCalledWithUserId_itShouldReturnUserViewResponse(){

        User user = generateUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserViewResponse result = adminService.getUser(user.getId());

        UserViewResponse response = UserViewResponse.convertUserViewResponse(user);
        assertEquals(result,response);
        verify(userRepository,times(1)).findById(user.getId());
    }

    @Test
    public void getUser_whenCalledWithNotExistUserId_itShouldThrowNullPointerException(){

        User user = generateUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class,() -> adminService.getUser(user.getId()));
    }



}