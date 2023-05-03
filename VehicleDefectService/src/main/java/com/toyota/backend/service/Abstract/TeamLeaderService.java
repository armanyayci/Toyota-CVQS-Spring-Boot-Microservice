package com.toyota.backend.service.Abstract;

import com.toyota.backend.dto.DefectViewResponse;

import java.util.List;

public interface TeamLeaderService {

    byte[] drawById(int id);

    List<DefectViewResponse> getListOfVehicleDefectsWithName(String name,int pageNo, int pageSize, String sortBy, int filterYear);

    List<DefectViewResponse> getListOfDefects(int pageNo, int pageSize, String sortBy,int filterYear);
}
