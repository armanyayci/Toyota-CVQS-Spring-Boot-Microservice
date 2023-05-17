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
 * This class represents a TT_Vehicle_Defect entity in the database.
 */
@Table(name = "TT_Vehicle_Defect")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TT_Vehicle_Defect {
    /**
     * The unique identifier for the TT_Vehicle_Defect entity.
     * The implementing strategy is defined as identity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The error id of the vehicle defect.
     */
    @Column(name = "errorId",length = 4)
    private int errorId;


    /**
     * The description of the vehicle defect.
     */
    @Column(name = "Description",nullable = false, length = 100)
    private String description;

    /**
     * The byte image of the vehicle defect.
     */
    @Column(name = "Img",nullable = false)
    @Lob
    private byte[] img;
    /**
     * The defect of the vehicle, represented as a
     * many-to-one relationship with the {@link TT_Vehicle} entity.
     * Cascading is set to ALL, fetching is set to EAGER.
     * @see TT_Vehicle
     */
    @JoinColumn(
            name = "Vehicle_ID",
            foreignKey = @ForeignKey(
                    name = "FK_VehicleId"
            ))
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            optional = false)
    private TT_Vehicle vehicle;
    /**
     * The location list of the defect, represented as a
     * OneToMany relationship with the {@link TT_Defect_Location} entity.
     * Cascading is set to ALL, fetching is set to EAGER.
     * @see TT_Defect_Location
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "ttVehicleDefect",
            targetEntity = TT_Defect_Location.class
    )
    private List<TT_Defect_Location> ttDetectLocations;
}
