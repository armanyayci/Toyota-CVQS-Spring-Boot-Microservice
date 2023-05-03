package com.toyota.backend.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class represents a TT_Vehicle entity in the database.
 */
@Table(name = "TT_Vehicle")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TT_Vehicle {
    /**
     * The unique identifier for the TT_Vehicle entity.
     * The implementing strategy is defined as identity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * The name of the vehicle.
     */
    @Column(name = "Name",nullable = false, length = 30)
    private String name;
    /**
     * The model year of the vehicle.
     */
    @Column(name = "Year",nullable = false,length = 4)
    private int model_year;
    /**
     * The body-type of the vehicle.
     */
    @Column(name = "VehicleBody",nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EVehicleBody vehicleBody;

    /**
     * The defects of the vehicles, represented as a OneToMany relationship with the {@link TT_Vehicle_Defect} entity.
     * Cascading is set to ALL, fetching is set to EAGER.
     * @see TT_Vehicle_Defect
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "vehicle",
            targetEntity = TT_Vehicle_Defect.class
    )
    private List<TT_Vehicle_Defect> ttVehicleDefect;
}