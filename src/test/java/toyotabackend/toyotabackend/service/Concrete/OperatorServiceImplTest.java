package toyotabackend.toyotabackend.service.Concrete;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import toyotabackend.toyotabackend.TestUtils;
import toyotabackend.toyotabackend.dao.VehicleDefectLocationRepository;
import toyotabackend.toyotabackend.dao.VehicleDefectRepository;
import toyotabackend.toyotabackend.dao.VehicleRepository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;
import toyotabackend.toyotabackend.dto.request.AddLocationDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OperatorServiceImplTest extends TestUtils {
    private OperatorServiceImpl operatorService;
    private VehicleRepository vehicleRepository;
    private VehicleDefectRepository vehicleDefectRepository;
    private VehicleDefectLocationRepository vehicleDefectLocationRepository;

    @BeforeEach
    void SetUp(){
        vehicleRepository= mock(VehicleRepository.class);
        vehicleDefectRepository = mock(VehicleDefectRepository.class);
        vehicleDefectLocationRepository = mock(VehicleDefectLocationRepository.class);
        operatorService = new OperatorServiceImpl(vehicleRepository,vehicleDefectRepository,vehicleDefectLocationRepository);
    }
    @Test
    public void upload_whenCalledWithJsonAndMultipartFile_itShouldSuccess(){

        String json = "{\"description\": \"test\", \"vehicle_id\": 1, \"x1\": 0, \"y1\": 0, \"x2\": 10, \"y2\": 10, \"x3\": 20, \"y3\": 20}";
        MultipartFile file = new MockMultipartFile("test.jpeg", new byte[0]);
        TT_Vehicle vehicle = generateVehicle();
        TT_Vehicle_Defect vehicleDefect = generateVehicleDefect();
        vehicleDefect.setImg(new byte[0]);

        vehicleDefect.setVehicle(vehicle);

        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
        when(vehicleDefectRepository.save(vehicleDefect)).thenReturn(vehicleDefect);

        List<TT_Vehicle_Defect> defectList = new ArrayList<>();
        defectList.add(vehicleDefect);
        when(vehicleDefectRepository.findAll()).thenReturn(defectList);

        TT_Defect_Location defectLocation = generateDefectLocation();
        defectLocation.setTtVehicleDefect(vehicleDefect);
        when(vehicleDefectLocationRepository.save(defectLocation)).thenReturn(defectLocation);

        operatorService.upload(json, file);

        assertEquals(1, defectList.size());

        assertEquals("test", vehicleDefect.getDescription());
        assertEquals(vehicle, vehicleDefect.getVehicle());
        assertNotNull(vehicleDefect.getImg());

        assertEquals(0, defectLocation.getX1());
        assertEquals(0, defectLocation.getY1());
        assertEquals(10, defectLocation.getX2());
        assertEquals(10, defectLocation.getY2());
        assertEquals(20, defectLocation.getX3());
        assertEquals(20, defectLocation.getY3());
        assertEquals(vehicleDefect, defectLocation.getTtVehicleDefect());
    }

    @Test
    public void upload_whenCalledWithNotExistVehicle_itShouldThrowNullPointerException(){

        String json = "{\"description\": \"test\", \"vehicle_id\": 1, \"x1\": 0, \"y1\": 0, \"x2\": 10, \"y2\": 10, \"x3\": 20, \"y3\": 20}";
        MultipartFile file = new MockMultipartFile("test.jpeg", new byte[0]);
        TT_Vehicle vehicle = generateVehicle();
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class,() -> operatorService.upload(json,file));
    }

    @Test
    public void addLocation_whenCalledWithAddLocationDto_itShouldSuccess(){

        TT_Vehicle_Defect defect = generateVehicleDefect();
        AddLocationDto dto = generateAddLocationDto();

        when(vehicleDefectRepository.findById(dto.getDefectId())).thenReturn(Optional.of(defect));

        TT_Defect_Location location = generateDefectLocation();
        location.setTtVehicleDefect(defect);
        when(vehicleDefectLocationRepository.save(location)).thenReturn(location);

        operatorService.addLocation(dto);

        assertEquals(dto.getX1(), location.getX1());
        assertEquals(dto.getY1(), location.getY1());
        assertEquals(dto.getX2(), location.getX2());
        assertEquals(dto.getY2(), location.getY2());
        assertEquals(dto.getX3(), location.getX3());
        assertEquals(dto.getY3(), location.getY3());
    }

    @Test
    public void addLocation_whenCalledWithNotExistVehicleDefect_itShouldThrowNullPointerException(){

        AddLocationDto dto = generateAddLocationDto();


        when(vehicleDefectRepository.findById(dto.getDefectId())).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class,() -> operatorService.addLocation(dto));
        verify(vehicleDefectLocationRepository,times(0)).save(any(TT_Defect_Location.class));


    }



}























