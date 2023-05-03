package com.toyota.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.toyota.backend.domain.TT_Defect_Location;

import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This interface is a Spring Data JPA repository
 * responsible for managing the persistence of {@link TT_Defect_Location} entities.
 * It provides methods to retrieve and persist information
 * in the database regarding vehicle defect locations.
 */
@Repository
public interface VehicleDefectLocationRepository extends JpaRepository<TT_Defect_Location,Integer> {
    /**
     * Retrieves a list of {@link TT_Defect_Location} entities by defect ID.
     * @param id The ID of the defect to search for.
     * @return A list of {@link TT_Defect_Location} entities that belong to the defect with the given ID.
     */
    @Query("select t from TT_Defect_Location t where t.ttVehicleDefect.id=:id")
    List<TT_Defect_Location> findLocationByDefectId(@Param("id") int id);
}
