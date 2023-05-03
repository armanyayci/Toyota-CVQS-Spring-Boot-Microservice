package com.toyota.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Arman Yaycı
 * @version 01.05.2023
 * A DTO (Data Transfer Object) class representing the description of a vehicle defect.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDefectDTO {
    /**
     * The description of the vehicle.
     */
    public String description;
}
