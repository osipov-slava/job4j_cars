package ru.job4j.cars.service;


import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface CarService {

    CarDto create(CarDto carDto);

    Optional<CarDto> findById(Long id);

    List<CarDto> findAll();

    List<CarDto> findAllByUser(UserDto userDto);

    boolean update(CarDto carDto, UserDto userDto);

    boolean deleteById(Long id, UserDto userDto);

}
