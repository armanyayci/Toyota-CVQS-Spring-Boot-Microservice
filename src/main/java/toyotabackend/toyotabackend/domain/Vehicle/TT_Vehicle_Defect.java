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
import java.sql.Blob;
import java.util.List;

@Table(name = "TT_Vehicle_Defect")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TT_Vehicle_Defect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Description",nullable = false, length = 100)
    private String description;

    @Column(name = "Img")
    @Lob
    private byte[] img;

    @JoinColumn(
            name = "Vehicle_ID",
            foreignKey = @ForeignKey(
                    name = "FK_VehicleId"
            )
    )
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            optional = false
    )
    private TT_Vehicle vehicle;


    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "ttVehicleDefect",
            targetEntity = TT_Defect_Location.class
    )
    private List<TT_Defect_Location> ttDetectLocations;









}
