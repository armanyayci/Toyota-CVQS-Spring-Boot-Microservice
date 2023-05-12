package com.toyota.backend.service.Abstract;

import com.toyota.backend.dto.DefectViewResponse;

import java.io.IOException;
import java.util.List;

public interface TeamLeaderService {

    byte[] drawById(int id) throws IOException;
    List<DefectViewResponse> getListOfVehicleDefectsWithName(String name,int pageNo, int pageSize, String sortBy, int filterYear);
    List<DefectViewResponse> getListOfDefects(int pageNo, int pageSize, String sortBy,int filterYear);
}
