package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import toyotabackend.toyotabackend.dto.response.DefectViewResponse;
import toyotabackend.toyotabackend.service.Concrete.TeamLeaderServiceImpl;

import java.util.List;


@RestController
@RequestMapping("/teamleader")
@RequiredArgsConstructor
public class TeamleaderController
{
    private final TeamLeaderServiceImpl teamLeaderService;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity <byte[]> getDrawnImage(@PathVariable("id") int id) {

            byte[] defect = teamLeaderService.drawById(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(defect,headers, HttpStatus.OK);
    }


    @GetMapping("vehicledefectlist/{name}")
    public ResponseEntity<List<DefectViewResponse>> ListOfVehicleDefects
            (@PathVariable("name") String name,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "20") int size,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "0") int filterYear){

        return ResponseEntity.ok(teamLeaderService.getListOfVehicleDefectsWithName(name,page,size,sortBy,filterYear));
    }

    @GetMapping("/defectlist")
    public ResponseEntity <List<DefectViewResponse>> listDefects
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "0") int filterYear) {

        return ResponseEntity.ok(teamLeaderService.getListOfDefects(page,size,sortBy,filterYear));
    }
}




















