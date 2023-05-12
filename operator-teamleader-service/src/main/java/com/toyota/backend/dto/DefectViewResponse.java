package com.toyota.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import com.toyota.backend.domain.TT_Defect_Location;
import com.toyota.backend.domain.TT_Vehicle_Defect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * A DTO class for converting TT_Vehicle_Defect entity objects to a response with necessary information
 * about the defect and its associated vehicle.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefectViewResponse
{
    private static final Logger logger = Logger.getLogger(DefectViewResponse.class);
    /**
     * The name of the associated vehicle.
     */
    String vehicle_name;
    /**
     * The body-type of the associated vehicle.
     */
    String bodyType;
    /**
     * The model_year of the associated vehicle.
     */
    Integer model_year;
    /**
     * The defect_description of the associated vehicle.
     */
    String defect_description;
    /**
     * The list of location of the associated vehicle defect.
     */
    List<String> defect_locations;
    /**
     * The drawn ID of the defect.
     */
    Integer DrawnId;
    /**
     * Converts a Page of TT_Vehicle_Defect objects to a list of DefectViewResponse objects.
     * @param defects the Page of TT_Vehicle_Defect objects to convert
     * @return a list of DefectViewResponse objects
     * @throws UnsupportedOperationException if the specified operation is not supported
     * @throws ClassCastException if the class of the specified element prevents it from being added to this list
     * @throws NullPointerException if the specified collection is null and this list does not permit null elements
     * @throws IllegalArgumentException if some property of a specified element prevents it from being added to this list
     */
    public static List<DefectViewResponse> ConvertToViewResponse(Page<TT_Vehicle_Defect> defects) {
        try {
            List<DefectViewResponse> responseList = new ArrayList<>();
            for (TT_Vehicle_Defect defect : defects) {
                DefectViewResponse response = DefectViewResponse.builder()
                        .defect_description(defect.getDescription())
                        .bodyType(defect.getVehicle().getVehicleBody().toString())
                        .defect_locations(defect.getTtDetectLocations().stream().map(TT_Defect_Location::getCordinate).collect(Collectors.toList()))
                        .model_year(defect.getVehicle().getModel_year())
                        .vehicle_name(defect.getVehicle().getName())
                        .DrawnId(defect.getId())
                        .build();
                responseList.add(response);
            }
            return responseList;
        } catch (UnsupportedOperationException | ClassCastException | NullPointerException |
                 IllegalArgumentException e) {
            logger.warn("responses couldn't add to list.");
            throw e;
        }
    }
}