package com.toyota.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.toyota.backend.dto.response.TerminalViewResponse;
import com.toyota.backend.service.Concrete.TerminalServiceImpl;

import java.util.List;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Controller class for managing terminal operations
 */
@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
public class TerminalController {
    private final TerminalServiceImpl terminalService;
    /**
    * Get a list of active terminals
    * @param page The page number for pagination
    * @param size The number of terminals to be returned in a single page
    * @param sortBy The field by which the returned terminals should be sorted
    * @param filterCategory The category by which the terminals should be filtered
    * @return A ResponseEntity object containing the list of terminals that satisfy the criteria
*/
    @GetMapping("/list")
    public ResponseEntity<List<TerminalViewResponse>> activeTerminals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String filterCategory){

        return ResponseEntity.ok(terminalService.getTerminals(page,size,sortBy,filterCategory));
    }

}
