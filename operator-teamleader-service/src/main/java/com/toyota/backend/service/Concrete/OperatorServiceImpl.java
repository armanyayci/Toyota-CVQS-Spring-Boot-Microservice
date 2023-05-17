package com.toyota.backend.service.Concrete;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.toyota.backend.dao.VehicleDefectLocationRepository;
import com.toyota.backend.dao.VehicleDefectRepository;
import com.toyota.backend.dao.VehicleRepository;
import com.toyota.backend.domain.TT_Defect_Location;
import com.toyota.backend.domain.TT_Vehicle;
import com.toyota.backend.domain.TT_Vehicle_Defect;
import com.toyota.backend.dto.AddLocationDto;
import com.toyota.backend.service.Abstract.OperatorService;

import java.io.IOException;
import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class provides an implementation for the {@link OperatorService} interface, handling all the business logic related to
 * operator roles operations on vehicle defects uploading. It contains methods to upload image and location of defects.
 * The class uses constructor injection to inject instances of
 * {@link VehicleRepository}, {@link VehicleDefectRepository}, {@link VehicleDefectLocationRepository}
 * The class also logs information and error messages using {@link Logger} to facilitate debugging and error handling.
 */
@Service
@RequiredArgsConstructor
public class OperatorServiceImpl implements OperatorService {

    private static final Logger logger = Logger.getLogger(OperatorServiceImpl.class);
    private final VehicleRepository vehicleRepository;
    private final VehicleDefectRepository vehicleDefectRepository;
    private final VehicleDefectLocationRepository vehicleDefectLocationRepository;


    /**
     * This method uploads the given JSON data and image file to create a new TT_Vehicle_Defect entity in the database.
     * @param json The JSON data containing the description and vehicle ID of the defect, as well as the coordinates of the defect location.
     * @param file The image file associated with the defect.
     * @throws JsonMappingException if there is an error while mapping the JSON data.
     * @throws JsonProcessingException if there is an error while processing the JSON data.
     * @throws IOException if there is an error while uploading the image file.
     */
    @Override //String json
    public void upload(MultipartFile file, String json){

        try {
            //used to map between Java objects and JSON data.
            ObjectMapper objectMapper = new ObjectMapper();
            //parse the json string into a JsonNode object, which represents the JSON data.
            JsonNode jsonNode = objectMapper.readTree(json);
            //extract the description and vehicle_id filed from the input as text
            String description = jsonNode.get("description").asText();
            int id = jsonNode.get("vehicleId").asInt();
            int error_id = jsonNode.get("errorId").asInt();
            //find the vehicle which we want to add defect to it.
            TT_Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException(String.format("Vehicle couldn't find with id: %s", id)));
            //if vehicle found. build a defect for the specified vehicle
            TT_Vehicle_Defect vehicleDefect = TT_Vehicle_Defect.builder()
                    .description(description)
                    .vehicle(vehicle)
                    .img(file.getBytes())
                    .errorId(error_id)
                    .build();
            vehicleDefectRepository.save(vehicleDefect);
            //find all the defect and get the last saved defect in order to add location in it.
            List<TT_Vehicle_Defect> entities = vehicleDefectRepository.findAll();
            TT_Vehicle_Defect savedVehicleDefect = entities.get(entities.size()-1);
            //add location into lastly saved defect.
            TT_Defect_Location defectLocation = TT_Defect_Location
                    .builder()
                    .x1(jsonNode.get("x1").asInt())
                    .y1(jsonNode.get("y1").asInt())
                    .x2(jsonNode.get("x2").asInt())
                    .y2(jsonNode.get("y2").asInt())
                    .x3(jsonNode.get("x3").asInt())
                    .y3(jsonNode.get("y3").asInt())
                    .ttVehicleDefect(savedVehicleDefect)
                    .build();
            vehicleDefectLocationRepository.save(defectLocation);
            logger.info(String.format("location added to defect, defect-id:%s, defect-locations: %s",savedVehicleDefect.getId(),defectLocation.getCordinate()));
        } catch (JsonMappingException e){
            logger.warn(String.format("error occurred while mapping the json in upload service. json: %s",json));
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            logger.warn(String.format("error occurred while processing the json in upload service. json: %s",json));
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn(String.format("error occurred while uploading file in upload service. file: %s",file));
            e.printStackTrace();
        }
    }
    /**
     * Adds a new location for a vehicle defect to the database.
     * A defect may have multiple locations. it is aimed to add locations into current defect.
     * @param dto an object containing the necessary information for adding the location (defect ID and coordinates)
     * @throws NullPointerException if the vehicle defect with the provided ID is not found in the database
     */
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
        logger.info(String.format("location added to defect, defect-id:%s, defect-locations: %s",defect.getId(),location.getCordinate()));

    }
}