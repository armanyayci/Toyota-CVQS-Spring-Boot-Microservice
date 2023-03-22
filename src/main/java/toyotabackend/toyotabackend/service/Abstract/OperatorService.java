package toyotabackend.toyotabackend.service.Abstract;

import org.springframework.web.multipart.MultipartFile;

public interface OperatorService {
    void upload(String json, MultipartFile file);

}
