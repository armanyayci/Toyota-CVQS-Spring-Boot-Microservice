package toyotabackend.toyotabackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLocationDto {
    @NotNull
    int x1;
    @NotNull
    int y1;
    @NotNull
    int x2;
    @NotNull
    int y2;
    @NotNull
    int x3;
    @NotNull
    int y3;
    @NotNull
    int defectId;
}
