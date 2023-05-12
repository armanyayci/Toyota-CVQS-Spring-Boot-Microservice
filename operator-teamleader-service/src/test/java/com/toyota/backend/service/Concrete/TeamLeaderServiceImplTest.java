package com.toyota.backend.service.Concrete;

import com.toyota.backend.TestUtils;
import com.toyota.backend.dao.VehicleDefectLocationRepository;
import com.toyota.backend.dao.VehicleDefectRepository;
import com.toyota.backend.domain.TT_Defect_Location;
import com.toyota.backend.domain.TT_Vehicle_Defect;
import com.toyota.backend.dto.DefectViewResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link TeamLeaderServiceImpl} class.
 */
class TeamLeaderServiceImplTest extends TestUtils {

    private TeamLeaderServiceImpl teamLeaderService;
    private VehicleDefectRepository vehicleDefectRepository;
    private VehicleDefectLocationRepository vehicleDefectLocationRepository;


    @BeforeEach
    void SetUp(){
        vehicleDefectRepository = mock(VehicleDefectRepository.class);
        vehicleDefectLocationRepository = mock(VehicleDefectLocationRepository.class);
        teamLeaderService = new TeamLeaderServiceImpl(vehicleDefectRepository,vehicleDefectLocationRepository);
    }

    @Test
    public void drawById_whenCalledWithVehicleDefectId_itShouldReturnByteOfImage() throws IOException {

        int defectId = 1;
        TT_Defect_Location location = generateDefectLocation();
        TT_Vehicle_Defect defect = generateVehicleDefect();
        defect.setImg(setDefectImage());


        when(vehicleDefectRepository.findById(defectId)).thenReturn(Optional.of(defect));
        when(vehicleDefectLocationRepository.findLocationByDefectId(defectId)).thenReturn(List.of(location));
        var result = teamLeaderService.drawById(defectId);
        assertNotNull(result);
    }

    @Test
    public void drawById_whenCalledWithNotExistVehicleDefect_itShouldThrowNullPointerException(){

        TT_Vehicle_Defect defect = generateVehicleDefect();
        when(vehicleDefectRepository.findById(defect.getId())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class,() -> teamLeaderService.drawById(defect.getId()));
    }

    @Test
    public void getListOfVehicleDefectsWithName_whenCalledWithVehicleNameAndOtherOptionalParameters_itShouldReturnListOfDefectViewResponse(){
        String name = "testName";
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        int filterYear = 2022;

        List<TT_Vehicle_Defect> defects = generateListOfVehicleDefects();
        Page<TT_Vehicle_Defect> defectPage = new PageImpl<>(defects);

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        when(vehicleDefectRepository.findDefectsByVehicleNameWithFilter(name, paging, filterYear)).thenReturn(defectPage);
        List<DefectViewResponse> result = teamLeaderService.getListOfVehicleDefectsWithName
                (name,pageNo,pageSize,sortBy,filterYear);

        assertNotNull(result);
        assertEquals(2,result.size());
    }

    @Test
    public void getListOfVehicleDefectsWithName_whenCalledWithVehicleNameWithoutFilterYear_itShouldReturnListOfDefectViewResponse(){
        String name = "testName";
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        int filterYear = 0;

        List<TT_Vehicle_Defect> defects = generateListOfVehicleDefects();
        Page<TT_Vehicle_Defect> defectPage = new PageImpl<>(defects);

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        when(vehicleDefectRepository.findDefectsByVehicleId(name, paging)).thenReturn(defectPage);
        List<DefectViewResponse> result = teamLeaderService.getListOfVehicleDefectsWithName
                (name,pageNo,pageSize,sortBy,filterYear);

        verify(vehicleDefectRepository,times(0)).findDefectsByVehicleNameWithFilter(name, paging, filterYear);
        assertNotNull(result);
        assertEquals(2,result.size());
    }

    @Test
    public void getListOfVehicleDefectsWithName_whenCalledNotExistDefectList_itShouldThrowNullPointerException(){

        String name = "testName";
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        int filterYear = 0;

        List<TT_Vehicle_Defect> defects = new ArrayList<>();
        Page<TT_Vehicle_Defect> defectPage = new PageImpl<>(defects);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        when(vehicleDefectRepository.findDefectsByVehicleId(name, paging)).thenReturn(defectPage);
        var result = teamLeaderService.getListOfVehicleDefectsWithName(name,pageNo,pageSize,sortBy,filterYear);

        assertEquals(result,new ArrayList<>());
        assertTrue(defectPage.isEmpty());
    }
    @Test
    public void getListOfDefects_whenCalledWithOptionalParametersWithoutFilterYear_itShouldReturnListOfDefectViewResponse(){

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        int filterYear = 0;

        List<TT_Vehicle_Defect> defects = generateListOfVehicleDefects();
        Page<TT_Vehicle_Defect> defectPage = new PageImpl<>(defects);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        when(vehicleDefectRepository.findAll(paging)).thenReturn(defectPage);
        List<DefectViewResponse> result = teamLeaderService.getListOfDefects(pageNo,pageSize,sortBy,filterYear);

        assertNotNull(result);
        assertEquals(2,result.size());
    }
    @Test
    public void getListOfDefects_whenCalledWithOptionalParametersWithFilterYear_itShouldReturnFilteredListOfDefectViewResponse(){

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        int filterYear = 2023;

        List<TT_Vehicle_Defect> defects = generateListOfVehicleDefects();
        Page<TT_Vehicle_Defect> defectPage = new PageImpl<>(defects);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        when(vehicleDefectRepository.findAllDefectWithFilter(paging,filterYear)).thenReturn(defectPage);
        List<DefectViewResponse> result = teamLeaderService.getListOfDefects(pageNo,pageSize,sortBy,filterYear);

        assertNotNull(result);
        assertEquals(2,result.size());
    }


    @Test
    public void getListOfDefects_whenCalledNotExistDefectList_itShouldThrowNullPointerException(){

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        int filterYear = 0;

        List<TT_Vehicle_Defect> defects = new ArrayList<>();
        Page<TT_Vehicle_Defect> defectPage = new PageImpl<>(defects);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        when(vehicleDefectRepository.findAll(paging)).thenReturn(defectPage);
        var result = teamLeaderService.getListOfDefects(pageNo,pageSize,sortBy,filterYear);

        assertEquals(result,new ArrayList<>());
        assertTrue(defectPage.isEmpty());
    }


}