package toyotabackend.toyotabackend.service.Abstract;

import toyotabackend.toyotabackend.dto.response.DefectViewResponse;

import java.util.List;

public interface TeamLeaderService {

    byte[] drawById(int id);

    List<DefectViewResponse> getListOfVehicleDefects(int id,int pageNo, int pageSize, String sortBy);

    List<DefectViewResponse> getListOfDefects(int pageNo, int pageSize, String sortBy );
}
