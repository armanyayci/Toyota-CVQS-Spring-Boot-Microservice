package com.toyota.backend;

import com.toyota.backend.domain.EVehicleBody;
import com.toyota.backend.domain.TT_Defect_Location;
import com.toyota.backend.domain.TT_Vehicle;
import com.toyota.backend.domain.TT_Vehicle_Defect;
import com.toyota.backend.dto.AddLocationDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Util class that helps to tests
 */
public class TestUtils {

    public static TT_Vehicle generateVehicle(){
        return TT_Vehicle.builder()
                .id(1)
                .name("testVehicleName")
                .model_year(1234)
                .vehicleBody(EVehicleBody.Coupe)
                .ttVehicleDefect(new ArrayList<>())
                .build();
    }
    public static TT_Vehicle_Defect generateVehicleDefect(){
        return TT_Vehicle_Defect.builder()
                .id(1)
                .description("test")
                .img(null)
                .vehicle(null)
                .ttDetectLocations(null)
                .build();
    }
    public static TT_Defect_Location generateDefectLocation(){
        return TT_Defect_Location.builder()
                .x1(0)
                .x2(10)
                .x3(20)
                .y1(0)
                .y2(10)
                .y3(20)
                .ttVehicleDefect(null)
                .build();
    }

    public static AddLocationDto generateAddLocationDto(){
        return AddLocationDto.builder()
                .defectId(1)
                .x1(0)
                .x2(10)
                .x3(20)
                .y1(0)
                .y2(10)
                .y3(20)
                .build();
    }
    public static List<TT_Defect_Location> generateListOfDefectLocation() {
        return IntStream.range(1,3).mapToObj(i->
                TT_Defect_Location.builder()
                        .id(i)
                        .x1(0)
                        .x2(10)
                        .x3(20)
                        .y1(0)
                        .y2(10)
                        .y3(20)
                        .ttVehicleDefect(generateVehicleDefect())
                        .build()).collect(Collectors.toList());
    }
    public static List<TT_Vehicle_Defect> generateListOfVehicleDefects() {
        return IntStream.range(1,3).mapToObj(i->
                TT_Vehicle_Defect.builder()
                        .id(i)
                        .img(new byte[0])
                        .vehicle(generateVehicle())
                        .description("testDescription")
                        .ttDetectLocations(generateListOfDefectLocation())
                        .build()).collect(Collectors.toList());
    }
    public static byte[] setDefectImage() throws IOException {
        File file = new File("src/test/java/com/toyota/backend/kapikolu.jpeg");
        BufferedImage image = ImageIO.read(file);
        ByteArrayOutputStream test = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", test);
        return test.toByteArray();
    }

}
