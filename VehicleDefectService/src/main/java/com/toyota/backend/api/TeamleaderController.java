package com.toyota.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.toyota.backend.dto.DefectViewResponse;
import com.toyota.backend.service.Concrete.TeamLeaderServiceImpl;

import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class provides methods for getting a drawn image,
 * listing defects for a given vehicle, and listing all defects.
 *
*/

@RestController
@RequestMapping("/teamleader")
@RequiredArgsConstructor
public class TeamleaderController
{
    private final TeamLeaderServiceImpl teamLeaderService;
    /**
     * Endpoint for getting the drawn image of a vehicle defect by id.
     * @param id The id of the vehicle defect.
     * @return ResponseEntity<byte[]> The image file as a byte array with content type set as image/jpeg.
     */
    @GetMapping(value = "/get/{id}")
    public ResponseEntity <byte[]> getDrawnImage(@PathVariable("id") int id) {
            byte[] defect = teamLeaderService.drawById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(defect,headers, HttpStatus.OK);
    }
    /**
     * Endpoint for getting a list of vehicle defects by name, sorted and filtered with pagination.
     * @param name The name of the vehicle to filter the defects by.
     * @param page The page number for pagination.
     * @param size The page size for pagination.
     * @param sortBy The property to sort the results by.
     * @param filterYear The year to filter the results by.
     * @return ResponseEntity<List<DefectViewResponse>> The list of vehicle defects as a response.
     */
    @GetMapping("vehicledefectlist/{name}")
    public ResponseEntity<List<DefectViewResponse>> ListOfVehicleDefectsByName
            (@PathVariable("name") String name,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "20") int size,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "0") int filterYear){

        return ResponseEntity.ok(teamLeaderService.getListOfVehicleDefectsWithName(name,page,size,sortBy,filterYear));
    }
    /**
    * This endpoint returns a list of defects.
    * The defects can be filtered by year and sorted by
    * id, name, or etc. It also supports pagination.
    * @param page The page number to return. Defaults to 0.
    * @param size The number of items per page. Defaults to 10.
    * @param sortBy The field to sort the results by. Defaults to "id".
    * @param filterYear that field filter results as year. Default brings all.
    */
    @GetMapping("/defectlist")
    public ResponseEntity <List<DefectViewResponse>> listDefects
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "0") int filterYear) {

        return ResponseEntity.ok(teamLeaderService.getListOfDefects(page,size,sortBy,filterYear));
    }
}




















