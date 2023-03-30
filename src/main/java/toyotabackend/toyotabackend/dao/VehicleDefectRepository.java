package toyotabackend.toyotabackend.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface VehicleDefectRepository extends JpaRepository<TT_Vehicle_Defect,Integer>
{
    @Query("select t from TT_Vehicle_Defect t where t.vehicle.id=:id")
    Page<TT_Vehicle_Defect> findDefectsByVehicleId(@Param("id") int id, Pageable pageable);
}
