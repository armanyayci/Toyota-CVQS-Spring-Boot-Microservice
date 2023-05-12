package com.toyota.backend.dao;

import com.toyota.backend.domain.TT_Defect_Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.toyota.backend.domain.TT_Vehicle;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This interface is a Spring Data JPA repository
 * responsible for managing the persistence of {@link TT_Vehicle} entities.
 * It provides methods to retrieve and persist information
 * in the database regarding vehicles.
 */
@Repository
public interface VehicleRepository extends JpaRepository<TT_Vehicle,Integer> {
}
