package toyotabackend.toyotabackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<TT_Vehicle,Integer>
{

}
