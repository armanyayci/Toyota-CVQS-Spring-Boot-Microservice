package toyotabackend.toyotabackend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teamleader")
public class TeamleaderController
{
    @GetMapping("/v1")
    public String test(){
        return "This is operator page.";
    }
}
