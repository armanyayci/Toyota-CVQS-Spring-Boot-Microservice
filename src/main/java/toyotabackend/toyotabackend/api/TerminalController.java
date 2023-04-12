package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toyotabackend.toyotabackend.dto.response.TerminalViewResponse;
import toyotabackend.toyotabackend.service.Concrete.TerminalServiceImpl;

import java.util.List;
@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
public class TerminalController {
    private final TerminalServiceImpl terminalService;

    @GetMapping("/list")
    public ResponseEntity<List<TerminalViewResponse>> activeTerminals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String filterCategory){

        return ResponseEntity.ok(terminalService.getTerminals(page,size,sortBy,filterCategory));
    }

}
