package com.toyota.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.toyota.backend.dto.AddLocationDto;
import com.toyota.backend.service.Concrete.OperatorServiceImpl;

import javax.validation.Valid;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This controller class handles incoming requests related to operators,
 * such as uploading data and adding location to defected vehicles.
 */
@RestController
@RequestMapping("/operator")
@RequiredArgsConstructor
public class OperatorController {
    private final OperatorServiceImpl operatorService;
    /**
     * Handles the POST request to upload a file and JSON data to the system.
     * @param json The JSON data to be uploaded.
     * @param file The file to be uploaded.
     * @return A ResponseEntity object with an "ok" status code if the upload is successful.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("json") String json,
            @RequestParam("file") MultipartFile file) {

            operatorService.upload(json,file);
            return ResponseEntity.ok("Vehicle Defect and Locations are saved");
    }
    /**
     * Handles the POST request to add a new location to a defected vehicle.
     * @param dto An AddLocationDto object containing the necessary information for the location addition.
     * @return A ResponseEntity object with an "ok" status code if the operation is successful.
     */
    @PostMapping("/addlocation")
    public ResponseEntity<String> addNewLocationToDefectedVehicle(@Valid @RequestBody AddLocationDto dto)
    {
        operatorService.addLocation(dto);
        return ResponseEntity.ok("Locations are added to vehicle.");
    }
}

