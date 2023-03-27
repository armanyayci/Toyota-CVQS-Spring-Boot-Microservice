package toyotabackend.toyotabackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleDefectLocationRepository extends JpaRepository<TT_Defect_Location,Integer> {

    @Query("select t from TT_Defect_Location t where t.ttVehicleDefect.id=:id")
    List<TT_Defect_Location> findLocationByDefectId(@Param("id") int id);
}
