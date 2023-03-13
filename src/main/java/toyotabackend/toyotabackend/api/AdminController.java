package toyotabackend.toyotabackend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toyotabackend.toyotabackend.dto.request.AddRemoveRoleDTO;
import toyotabackend.toyotabackend.dto.request.UpdateUserDTO;
import toyotabackend.toyotabackend.dto.response.UserViewResponse;
import toyotabackend.toyotabackend.dto.response.AddedUserResponse;
import toyotabackend.toyotabackend.dto.request.RegisterDTO;
import toyotabackend.toyotabackend.dto.response.UpdatedUserResponse;
import toyotabackend.toyotabackend.service.Concrete.AdminServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;

    @PostMapping("/add")
    public ResponseEntity<AddedUserResponse> add(@Valid @RequestBody RegisterDTO registerDTO){

        return ResponseEntity.ok(adminServiceImpl.adminAdded(registerDTO));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteUser(@Valid @PathVariable("id") int id) {
        adminServiceImpl.softDeleteUser(id);
        return ResponseEntity.ok("User is soft deleted");
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<String> softActiveUser(@Valid @PathVariable("id") int id) {
        adminServiceImpl.softActiveUser(id);
        return ResponseEntity.ok("User is activated");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdatedUserResponse> updateUser (@Valid @PathVariable("id") int id, @RequestBody UpdateUserDTO dto) {

        return ResponseEntity.ok(adminServiceImpl.updateUser(dto,id));
    }

    @PostMapping("/addrole")
    public ResponseEntity<String> addRole(@Valid @RequestBody AddRemoveRoleDTO dto){

        adminServiceImpl.addRole(dto);
        return ResponseEntity.ok("User role is added with id -> "+ dto.getRoleId());
    }

    @PostMapping("/removerole")
    public ResponseEntity<String> removeRole(@Valid @RequestBody AddRemoveRoleDTO dto){

        adminServiceImpl.removeRole(dto);
        return ResponseEntity.ok("User role is deleted with id -> " + dto.getRoleId());
    }

    @GetMapping("/getusers")
    public ResponseEntity<List<UserViewResponse>>getUsers(){

        return ResponseEntity.ok(adminServiceImpl.getUsers());
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<UserViewResponse> getUser(@PathVariable("id") int id ){
        return ResponseEntity.ok(adminServiceImpl.getUser(id));
    }
}
