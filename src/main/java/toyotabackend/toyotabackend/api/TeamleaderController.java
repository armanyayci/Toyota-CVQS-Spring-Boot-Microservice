package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity <byte[]> getFile(@PathVariable("id") int id) {

            byte[] defect = teamLeaderService.drawById(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(defect,headers, HttpStatus.OK);
    }


    @GetMapping("vehicledefectlist/{id}")
    public ResponseEntity<List<DefectViewResponse>> ListOfVehicleDefects
            (@PathVariable("id") int id,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "id") String sortBy){


        return ResponseEntity.ok(teamLeaderService.getListOfVehicleDefects(id,page,size,sortBy));
    }

    @GetMapping("/defectlist")
    public ResponseEntity <List<DefectViewResponse>> listDefects
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "id") String sortBy)
    {
        return ResponseEntity.ok(teamLeaderService.getListOfDefects(page,size,sortBy));
    }

}




















