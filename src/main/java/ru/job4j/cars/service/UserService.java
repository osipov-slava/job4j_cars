package ru.job4j.cars.service;

import ru.job4j.cars.dto.UserDto;

import java.util.Optional;

public interface UserService {

    UserDto create(UserDto userDto);

    Optional<UserDto> findByEmailAndPassword(String email, String password);

}
