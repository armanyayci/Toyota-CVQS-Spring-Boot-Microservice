package com.toyota.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.backend.TestUtils;
import com.toyota.backend.dto.DefectViewResponse;
import com.toyota.backend.service.Concrete.TeamLeaderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link TeamleaderController} class.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TeamleaderController.class)
class TeamleaderControllerTest extends TestUtils {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamLeaderServiceImpl teamLeaderService;

    @Test
    public void getDrawnImage_whenCalledWithByteArrayAndDefectId_itShouldReturnOk() throws Exception{
        byte[] imageData = "your-image-data-test".getBytes();
        int defectId = 1;

        ResultActions resultActions = mockMvc.perform(get("/teamleader/get/" + defectId))
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE));

        when(teamLeaderService.drawById(defectId)).thenReturn(imageData);
        verify(teamLeaderService,times(1)).drawById(defectId);
        resultActions.andExpect(status().isOk());
    }
    @Test
    public void listOfVehicleDefectsByName_whenCalledWithName_itShouldReturnOk() throws Exception{

        String name = "testName";
        int page = 0;
        int size = 20;
        String sortBy = "id";
        int filterYear = 0;
        ResultActions resultActions = mockMvc.perform(get("/teamleader/defectlist/" + name)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("filterYear", String.valueOf(filterYear))
                        .contentType(MediaType.APPLICATION_JSON));
        when(teamLeaderService.getListOfVehicleDefectsWithName(name,page,size,sortBy,filterYear)).thenReturn(List.of(new DefectViewResponse()));
        verify(teamLeaderService,times(1)).getListOfVehicleDefectsWithName(name,page,size,sortBy,filterYear);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void listDefects_whenCalled_itShouldReturnOK() throws Exception{
        int page = 0;
        int size = 20;
        String sortBy = "id";
        int filterYear = 0;
        ResultActions resultActions = mockMvc.perform(get("/teamleader/defectlist")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sortBy", sortBy)
                .param("filterYear", String.valueOf(filterYear))
                .contentType(MediaType.APPLICATION_JSON));
        when(teamLeaderService.getListOfDefects(page,size,sortBy,filterYear)).thenReturn(List.of(new DefectViewResponse()));
        verify(teamLeaderService,times(1)).getListOfDefects(page,size,sortBy,filterYear);
        resultActions.andExpect(status().isOk());
    }
















}