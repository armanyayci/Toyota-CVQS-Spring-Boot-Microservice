package toyotabackend.toyotabackend;

import toyotabackend.toyotabackend.domain.Entity.Role;
import toyotabackend.toyotabackend.domain.Entity.User;
import toyotabackend.toyotabackend.domain.Vehicle.EVehicleBody;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Defect_Location;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle;
import toyotabackend.toyotabackend.domain.Vehicle.TT_Vehicle_Defect;
import toyotabackend.toyotabackend.dto.request.*;
import toyotabackend.toyotabackend.dto.response.JwtResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

    public static RegisterDTO generateRegisterDto() {
        return RegisterDTO.builder()
                .name("test")
                .username("testUsername")
                .email("testMail@mail.com")
                .password("1234")
                .roleId(1)
                .build();
    }
    public static Role generateRole(){
        return Role.builder()
                .id(1)
                .name("testRole")
                .users(new ArrayList<>())
                .build();
    }
    public static User generateUser(){
        return User.builder()
                .id(1)
                .name("Test")
                .email("test@test.com")
                .isActive(true)
                .password("testPassword")
                .username("testUsername")
                .roles(new ArrayList<>())
                .build();
    }
    public static UpdateUserDTO generateUpdateUserDto(){
        return UpdateUserDTO.builder()
                .email("test@test.com")
                .username("testUsername")
                .name("test")
                .password("encodedPassword")
                .build();
    }
    public static AddRemoveRoleDTO generateAddRemoveRoleDto(){
        return AddRemoveRoleDTO.builder()
                .username("testUsername")
                .roleId(1)
                .build();
    }
    public static List<User> generateUserList() {
        return IntStream.range(2,5).mapToObj(i->
                User.builder()
                        .id(11)
                        .name("testName")
                        .email("testmail@mail.com")
                        .username("testUsername")
                        .password("testPassword")
                        .isActive(true)
                        .roles(new ArrayList<>())
                        .build()).collect(Collectors.toList());
    }
    public static LoginDTO generateLoginDto(){
        return LoginDTO.builder()
                .username("testUsername")
                .password("testPassword")
                .build();
    }
    public static JwtResponse generateJwtResponse(){
        return JwtResponse.builder()
                .Token("token")
                .build();
    }
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













}
