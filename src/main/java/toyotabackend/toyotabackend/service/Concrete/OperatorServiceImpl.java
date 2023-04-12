package toyotabackend.toyotabackend.service.Concrete;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import toyotabackend.toyotabackend.dao.VehicleDefectLocationRepository;
import toyotabackend.toyotabackend.dao.VehicleDefectRepository;
import toyotabackend.toyotabackend.dao.VehicleRepository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;
import toyotabackend.toyotabackend.dto.request.AddLocationDto;
import toyotabackend.toyotabackend.service.Abstract.OperatorService;

import java.io.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperatorServiceImpl implements OperatorService {

    private static final Logger logger = Logger.getLogger(OperatorServiceImpl.class);

    private final VehicleRepository vehicleRepository;
    private final VehicleDefectRepository vehicleDefectRepository;
    private final VehicleDefectLocationRepository vehicleDefectLocationRepository;
    @Override
    public void upload(String json, MultipartFile file){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            String description = jsonNode.get("description").asText();
            Integer id = jsonNode.get("vehicle_id").asInt();

            TT_Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("Vehicle couldn't find with id: %s", id)));

            TT_Vehicle_Defect vehicleDefect = TT_Vehicle_Defect.builder()
                    .description(description)
                    .vehicle(vehicle)
                    .img(file.getBytes())
                    .build();

            vehicleDefectRepository.save(vehicleDefect);
            List<TT_Vehicle_Defect> entities = vehicleDefectRepository.findAll();
            TT_Vehicle_Defect savedVehicleDefect = entities.get(entities.size()-1);

            int x1 = jsonNode.get("x1").asInt();
            int y1 = jsonNode.get("y1").asInt();
            int x2 = jsonNode.get("x2").asInt();
            int y2 = jsonNode.get("y2").asInt();
            int x3 = jsonNode.get("x3").asInt();
            int y3 = jsonNode.get("y3").asInt();

            TT_Defect_Location defectLocation = TT_Defect_Location
                    .builder()
                    .x1(x1)
                    .y1(y1)
                    .x2(x2)
                    .y2(y2)
                    .x3(x3)
                    .y3(y3)
                    .ttVehicleDefect(savedVehicleDefect)
                    .build();
            vehicleDefectLocationRepository.save(defectLocation);
        } catch (JsonMappingException e){
            logger.warn("error occurred while mapping the json in upload service.");
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            logger.warn("error occurred while processing the json in upload service.");
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn("error occurred while uploading file in upload service.");
            e.printStackTrace();
        }
    }

    @Override
    public void addLocation(AddLocationDto dto) {

        TT_Vehicle_Defect defect = vehicleDefectRepository.findById(dto.getDefectId()).orElseThrow(
                ()-> new NullPointerException(String.format("defect not found with id: %s ", dto.getDefectId())));

        TT_Defect_Location location = TT_Defect_Location.builder()
                .ttVehicleDefect(defect)
                .x1(dto.getX1())
                .x2(dto.getX2())
                .x3(dto.getX3())
                .y1(dto.getY1())
                .y2(dto.getY2())
                .y3(dto.getY3()).build();
        vehicleDefectLocationRepository.save(location);
    }
}
























