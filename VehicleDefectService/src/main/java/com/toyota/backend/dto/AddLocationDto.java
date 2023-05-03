package com.toyota.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * A DTO class for holding information of adding a location to a vehicle defect.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLocationDto {
    /**
     * The x coordinate of the first point of the location.
     */
    @NotNull
    int x1;
    /**
     * The y coordinate of the first point of the location.
     */
    @NotNull
    int y1;
    /**
     * The x coordinate of the second point of the location.
     */
    @NotNull
    int x2;
    /**
     * The y coordinate of the second point of the location.
     */
    @NotNull
    int y2;
    /**
     * The x coordinate of the third point of the location.
     */
    @NotNull
    int x3;
    /**
     * The y coordinate of the third point of the location.
     */
    @NotNull
    int y3;
    /**
     * The ID of the defect that this location belongs to.
     */
    @NotNull
    int defectId;
}
