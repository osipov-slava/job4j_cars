package ru.job4j.cars.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String email;

    private String login;

    private String password;

    private Long ownerId;

    private String name;

    private String timezone;

}
