package toyotabackend.toyotabackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;

import java.util.Optional;

@Repository
public interface VehicleDefectLocationRepository extends JpaRepository<TT_Defect_Location,Integer> {


}
