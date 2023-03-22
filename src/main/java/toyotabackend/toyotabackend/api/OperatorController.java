package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import toyotabackend.toyotabackend.service.Concrete.OperatorServiceImpl;

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
}

