package ru.job4j.cars.service;

import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.User;

import java.util.Optional;

public interface UserService {

    UserDto save(UserDto userDto);

    Optional<UserDto> findByEmailAndPassword(String email, String password);
}
