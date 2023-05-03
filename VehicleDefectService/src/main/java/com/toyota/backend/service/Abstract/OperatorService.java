package com.toyota.backend.service.Abstract;

import org.springframework.web.multipart.MultipartFile;
import com.toyota.backend.dto.AddLocationDto;

public interface OperatorService {
    void upload(String json, MultipartFile file);

    void addLocation(AddLocationDto dto);

}
