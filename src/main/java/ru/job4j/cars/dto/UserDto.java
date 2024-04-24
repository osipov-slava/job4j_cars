package ru.job4j.cars.dto;

import lombok.Data;

@Data
public class UserDto {

    private int id;

    private String email;

    private String login;

    private String password;

    private int ownerId;

    private String name;

}
