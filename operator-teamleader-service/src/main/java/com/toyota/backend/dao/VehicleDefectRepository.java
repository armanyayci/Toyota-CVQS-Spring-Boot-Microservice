package com.toyota.backend.dao;


import com.toyota.backend.domain.TT_Defect_Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.toyota.backend.domain.TT_Vehicle_Defect;

import javax.transaction.Transactional;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This interface is a Spring Data JPA repository
 * responsible for managing the persistence of {@link TT_Vehicle_Defect} entities.
 * It provides methods to retrieve and persist information
 * in the database regarding vehicle defects.
 */
@Repository
@Transactional
public interface VehicleDefectRepository extends JpaRepository<TT_Vehicle_Defect,Integer> {
    /**
     * Retrieves a page of vehicle defects for a given vehicle name.
     * @param name     the name of the vehicle to retrieve the defects for
     * @param pageable the pagination and sorting information
     * @return a page of vehicle defects
     */
    @Query("select t from TT_Vehicle_Defect t where t.vehicle.name=:name")
    Page<TT_Vehicle_Defect> findDefectsByVehicleId(@Param("name") String name, Pageable pageable);

    /**
     * Retrieves a page of vehicle defects for a given vehicle name and model year.
     * @param name      the name of the vehicle to retrieve the defects for
     * @param pageable  the pagination and sorting information
     * @param year the model year of the vehicle to retrieve the defects for
     * @return a page of vehicle defects
     */
    @Query("select t from TT_Vehicle_Defect t where t.vehicle.name=:name and t.vehicle.model_year =:year")
    Page<TT_Vehicle_Defect> findDefectsByVehicleNameWithFilter(@Param("name") String name, Pageable pageable,@Param("year") int year);

    /**
     * Retrieves a page of all vehicle defects for a given model year.
     * @param pageable  the pagination and sorting information
     * @param modelYear the model year of the vehicle to retrieve the defects for
     * @return a page of vehicle defects
     */
    @Query("select t from TT_Vehicle_Defect t where t.vehicle.model_year =:modelYear")
    Page<TT_Vehicle_Defect> findAllDefectWithFilter(Pageable pageable, @Param("modelYear") int modelYear);
}
