package toyotabackend.toyotabackend.domain.Vehicle;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ByteArraySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "TT_Vehicle")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TT_Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name",nullable = false, length = 30)
    private String name;
    @Column(name = "Year",nullable = false,length = 4)
    private int model_year;
    @Column(name = "VehicleBody",nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EVehicleBody vehicleBody;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "vehicle",
            targetEntity = TT_Vehicle_Defect.class
    )
    private List<TT_Vehicle_Defect> ttVehicleDefect;


}














