package toyotabackend.toyotabackend.service.Abstract;

import org.springframework.web.multipart.MultipartFile;
import toyotabackend.toyotabackend.dto.request.AddLocationDto;

public interface OperatorService {
    void upload(String json, MultipartFile file);

    void addLocation(AddLocationDto dto);

}
