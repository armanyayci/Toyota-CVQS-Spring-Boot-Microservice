package com.toyota.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.backend.TestUtils;
import com.toyota.backend.domain.Entity.User;
import com.toyota.backend.dto.request.AddRemoveRoleDTO;
import com.toyota.backend.dto.request.RegisterDTO;
import com.toyota.backend.dto.request.UpdateUserDTO;
import com.toyota.backend.service.Concrete.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link AdminController} class.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest extends TestUtils {
    private final static String CONTENT_TYPE = "application/json";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdminServiceImpl adminService;
    @Test
    public void add_whenCalledWithValidRegisterDto_itShouldReturnOk() throws Exception{

        RegisterDTO dto = generateRegisterDto();

        ResultActions actions = mockMvc.perform(post("/admin/add")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(dto)));

        ArgumentCaptor<RegisterDTO> captor = ArgumentCaptor.forClass(RegisterDTO.class);
        verify(adminService, times(1)).AuthorizeNewUser(captor.capture());
        actions.andExpect(status().isOk());
    }
    @Test
    public void softDeleteUser_whenCalledWithUsername_itShouldReturnOk()throws Exception{

        User user = generateUser();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/delete/{username}", user.getUsername())
                        .contentType(CONTENT_TYPE))
                .andExpect(content().string("User is soft deleted"));

        verify(adminService, times(1)).softDeleteUser(user.getUsername());
        actions.andExpect(status().isOk());

    }
    @Test
    public void softActiveUser_whenCalledWithUsername_itShouldReturnOk()throws Exception{

        User user = generateUser();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/active/{username}", user.getUsername())
                        .contentType(CONTENT_TYPE))
                .andExpect(content().string("User is activated"));
        verify(adminService,times(1)).softActiveUser(user.getUsername());
        actions.andExpect(status().isOk());
    }
    @Test
    public void updateUser_whenCalledWithValidUpdateUserDto_itShouldReturnOk()throws Exception{
        User user = generateUser();
        UpdateUserDTO dto = generateUpdateUserDto();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/update/{username}", user.getUsername())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(dto)));

        verify(adminService, times(1)).updateUser(dto,user.getUsername());
        actions.andExpect(status().isOk());
    }

    @Test
    public void addRole_whenCalledWithValidAddRemoveRoleDto_itShouldReturnOk()throws Exception{

        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();
        ResultActions actions = mockMvc.perform(post("/admin/addrole/")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(dto)));
        verify(adminService, times(1)).addRole(dto);
        actions.andExpect(status().isOk());
    }

    @Test
    public void removeRole_whenCalledWithValidAddRemoveRoleDto_itShouldReturnOk()throws Exception{
        AddRemoveRoleDTO dto = generateAddRemoveRoleDto();

        ResultActions actions = mockMvc.perform(post("/admin/removerole/")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(dto)));
        verify(adminService, times(1)).removeRole(dto);
        actions.andExpect(status().isOk());
    }
    @Test
    public void getActiveUsers_whenCalled_itShouldReturnOk()throws Exception{
        int page = 0;
        int size = 4;
        String sortBy = "id";
        String filter = "";
        ResultActions actions = mockMvc.perform(get("/admin/getactiveusers/")
                .contentType(CONTENT_TYPE));
        verify(adminService, times(1)).getActiveUsers(page,size,sortBy,filter);
        actions.andExpect(status().isOk());
    }
    @Test
    public void getUser_whenCalledWithSpecificUsername_itShouldReturnOk()throws Exception{

        User user = generateUser();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/getuser/{username}", user.getUsername())
                        .contentType(CONTENT_TYPE));
        verify(adminService, times(1)).getUser(user.getUsername());
        actions.andExpect(status().isOk());
    }

    @Test
    public void allUsers_whenCalled_itShouldReturnOk()throws Exception{
        int page = 0;
        int size = 4;
        String sortBy = "id";
        String filter = "";
        ResultActions actions = mockMvc.perform(get("/admin/getallusers/")
                .contentType(CONTENT_TYPE));
        verify(adminService, times(1)).getAllUsers(page,size,sortBy,filter);
        actions.andExpect(status().isOk());
    }

}









































