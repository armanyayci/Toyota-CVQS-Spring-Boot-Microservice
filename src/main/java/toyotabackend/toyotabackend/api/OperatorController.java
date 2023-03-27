package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import toyotabackend.toyotabackend.dto.request.AddLocationDto;
import toyotabackend.toyotabackend.service.Concrete.OperatorServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/operator")
@RequiredArgsConstructor
public class OperatorController {
    private final OperatorServiceImpl operatorService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("json") String json,
            @RequestParam("file") MultipartFile file) {

            operatorService.upload(json,file);

            return ResponseEntity.ok("saved");

    }

    @PostMapping("addlocation")
    public ResponseEntity<String> addNewLocationToDefectedVehicle(@Valid @RequestBody AddLocationDto dto)
    {
        operatorService.addLocation(dto);

        return ResponseEntity.ok("Location add operation succeed.");
    }
















}

