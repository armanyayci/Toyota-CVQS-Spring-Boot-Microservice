package com.toyota.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.toyota.backend.TestUtils;
import com.toyota.backend.dto.AddLocationDto;
import com.toyota.backend.service.Concrete.OperatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link OperatorController} class.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OperatorController.class)
class OperatorControllerTest extends TestUtils {
    private final static String CONTENT_TYPE = "application/json";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OperatorServiceImpl operatorService;

    @Test
    public void uploadFile_CalledWithJsonAndMultiPartFile_itShouldReturnOK() throws Exception {

        String json = "jsonTest";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file content".getBytes());
        MockHttpServletRequestBuilder postRequest = multipart("/operator/upload")
                .file(file)
                .param("json", json);
        MvcResult result = mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        verify(operatorService,times(1)).upload(json,file);
        assertEquals("Vehicle Defect and Locations are saved", responseBody);
    }
    @Test
    public void addLocations_CalledWithAddLocationDto_itShouldReturnOk() throws Exception{

        AddLocationDto dto = generateAddLocationDto();
        ResultActions actions = mockMvc.perform(post("/operator/addlocation/")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(dto)));
        verify(operatorService, times(1)).addLocation(dto);
        actions.andExpect(status().isOk());
        actions.andExpect(content().string("Locations are added to vehicle."));
    }
}























