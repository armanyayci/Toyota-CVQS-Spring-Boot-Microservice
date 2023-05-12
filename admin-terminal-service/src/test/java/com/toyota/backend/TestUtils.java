package com.toyota.backend;

import com.toyota.backend.domain.Entity.Role;
import com.toyota.backend.domain.Entity.User;
import com.toyota.backend.domain.Terminal.TerminalCategory;
import com.toyota.backend.dto.request.*;
import com.toyota.backend.dto.response.UserViewResponse;

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
                .email("testMail@test.com")
                .username("UsernameTest")
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
    public static List<TerminalCategory> generateListOfTerminalCategory() {
        return IntStream.range(1,3).mapToObj(i->
                TerminalCategory.builder()
                        .id(i)
                        .terminals(new ArrayList<>())
                        .name("testName")
                        .build()).collect(Collectors.toList());
    }
    public static List<UserViewResponse> generateUserViewResponse() {
        return IntStream.range(1,3).mapToObj(i->
                UserViewResponse.builder()
                        .name("test")
                        .email("testMail")
                        .activeness(true)
                        .roleList(List.of("role"))
                        .username("testUsername")
                        .build()).collect(Collectors.toList());
    }



}

