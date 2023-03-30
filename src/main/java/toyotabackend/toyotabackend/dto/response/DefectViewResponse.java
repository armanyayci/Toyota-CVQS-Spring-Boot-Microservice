package toyotabackend.toyotabackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefectViewResponse
{
    String vehicle_name;
    String bodyType;
    Integer model_year;
    String defect_description;
    List<String> defect_locations;
    Integer DrawnId;

    public static List<DefectViewResponse> ConvertToViewResponse(Page<TT_Vehicle_Defect> defects)
    {
        List<DefectViewResponse> responseList = new ArrayList<>();
        for (TT_Vehicle_Defect defect : defects)
        {
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
    }
}