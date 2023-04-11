package toyotabackend.toyotabackend.service.Abstract;

import toyotabackend.toyotabackend.dto.response.DefectViewResponse;

import java.util.List;

public interface TeamLeaderService {

    byte[] drawById(int id);

    List<DefectViewResponse> getListOfVehicleDefectsWithName(String name,int pageNo, int pageSize, String sortBy, int filterYear);

    List<DefectViewResponse> getListOfDefects(int pageNo, int pageSize, String sortBy,int filterYear);
}
