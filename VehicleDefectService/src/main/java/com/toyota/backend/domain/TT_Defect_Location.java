package com.toyota.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * This class represents a TT_Detect_Location entity in the database.
 */
@Table(name = "TT_Detect_Location")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TT_Defect_Location {
    /**
     * The unique identifier for the TT_Defect_Location entity.
     * The implementing strategy is defined as identity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * The x-coordinate of the first point that defines the location of the defect.
     */
    @Column(name = "x1",nullable = false)
    private int x1;
    /**
     * The y-coordinate of the first point that defines the location of the defect.
     */
    @Column(name = "y1",nullable = false)
    private int y1;
    /**
     * The x-coordinate of the second point that defines the location of the defect.
     */
    @Column(name = "x2",nullable = false)
    private int x2;
    /**
     * The y-coordinate of the second point that defines the location of the defect.
     */
    @Column(name = "y2",nullable = false)
    private int y2;
    /**
     * The x-coordinate of the third point that defines the location of the defect.
     */
    @Column(name = "x3",nullable = false)
    private int x3;
    /**
     * The y-coordinate of the third point that defines the location of the defect.
     */
    @Column(name = "y3",nullable = false)
    private int y3;
    /**
     * The defect location of the defect, represented as a
     * many-to-one relationship with the {@link TT_Vehicle_Defect} entity.
     * Cascading is set to ALL, fetching is set to EAGER.
     * @see TT_Vehicle_Defect
     */
    @JoinColumn(
            name = "VehicleDefect_Id",
            foreignKey = @ForeignKey(
                    name = "FK_VehicleDefect_ID"
            ))
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            optional = false
    )
    private TT_Vehicle_Defect ttVehicleDefect;
    /**
     * Returns a string representation of the coordinates that define the location of the defect.
     * @return a string in the format [P(x1,y1),P(x2,y2),P(x3,y3)]
     */
    public String getCordinate() {
        return String.format("[P(%s,%s),P(%s,%s),P(%s,%s)",x1,y1,x2,y2,x3,y3);
    }
}
