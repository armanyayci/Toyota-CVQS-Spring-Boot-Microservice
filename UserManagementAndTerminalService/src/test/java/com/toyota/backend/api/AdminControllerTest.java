package com.toyota.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.backend.TestUtils;
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
    public void softDeleteUser_whenCalledWithUserId_itShouldReturnOk()throws Exception{

        int userId = 1;
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/delete/{id}", userId)
                        .contentType(CONTENT_TYPE))
                .andExpect(content().string("User is soft deleted"));

        verify(adminService, times(1)).softDeleteUser(userId);
        actions.andExpect(status().isOk());

    }
    @Test
    public void softActiveUser_whenCalledWithUserId_itShouldReturnOk()throws Exception{

        int userId = 1;
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/active/{id}", userId)
                        .contentType(CONTENT_TYPE))
                .andExpect(content().string("User is activated"));
        verify(adminService,times(1)).softActiveUser(userId);
        actions.andExpect(status().isOk());
    }
    @Test
    public void updateUser_whenCalledWithValidUpdateUserDto_itShouldReturnOk()throws Exception{
        int userId = 1;
        UpdateUserDTO dto = generateUpdateUserDto();
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/admin/update/{id}", userId)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(dto)));

        verify(adminService, times(1)).updateUser(dto,userId);
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
    public void getUsers_whenCalled_itShouldReturnOk()throws Exception{

        ResultActions actions = mockMvc.perform(get("/admin/getusers/")
                .contentType(CONTENT_TYPE));
        verify(adminService, times(1)).getUsers();
        actions.andExpect(status().isOk());
    }
    @Test
    public void getUser_whenCalledWithSpecificUserId_itShouldReturnOk()throws Exception{

        int userId = 1;
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/admin/getuser/{id}", userId)
                        .contentType(CONTENT_TYPE));
        verify(adminService, times(1)).getUser(userId);
        actions.andExpect(status().isOk());
    }
}









































