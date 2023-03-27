package toyotabackend.toyotabackend.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface VehicleDefectRepository extends JpaRepository<TT_Vehicle_Defect,Integer>
{

}
