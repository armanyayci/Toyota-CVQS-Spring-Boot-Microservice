package toyotabackend.toyotabackend;

import toyotabackend.toyotabackend.domain.Entity.Role;
import toyotabackend.toyotabackend.domain.Entity.User;
import toyotabackend.toyotabackend.dto.request.AddRemoveRoleDTO;
import toyotabackend.toyotabackend.dto.request.RegisterDTO;
import toyotabackend.toyotabackend.dto.request.UpdateUserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

    public static RegisterDTO generateRegisterDto()
    {
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

}
