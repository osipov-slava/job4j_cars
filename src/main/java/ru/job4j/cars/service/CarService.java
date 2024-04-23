package ru.job4j.cars.service;


import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public interface CarService {

    Car create(Car car, User user, List<Integer> categoryIds);

    boolean update(int id, Car car, List<Integer> categoryIds);

    boolean deleteById(int id, User user);

    Optional<Car> findById(int id, User user);

    List<Car> findAll(User user);

}
