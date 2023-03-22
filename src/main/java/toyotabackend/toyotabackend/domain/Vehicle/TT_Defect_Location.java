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

@Table(name = "TT_Detect_Location")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TT_Defect_Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "x1",nullable = false)
    private int x1;
    @Column(name = "y1",nullable = false)
    private int y1;
    @Column(name = "x2",nullable = false)
    private int x2;
    @Column(name = "y2",nullable = false)
    private int y2;
    @Column(name = "x3",nullable = false)
    private int x3;
    @Column(name = "y3",nullable = false)
    private int y3;


    @JoinColumn(
            name = "VehicleDefect_Id",
            foreignKey = @ForeignKey(
                    name = "FK_VehicleDefect_ID"
            )
    )
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            optional = false
    )
    private TT_Vehicle_Defect ttVehicleDefect;



    public String getCordinate() {
        return String.format("[P(%s,%s}),P({%s,%s),P(%s,%s)",x1,y1,x2,y2,x3,y3);
    }


}
