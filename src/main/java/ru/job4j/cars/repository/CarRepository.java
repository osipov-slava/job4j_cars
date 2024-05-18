package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository {

    Car create(Car car);

    List<Car> findAll();

    List<Car> findAllByOwnerId(int ownerId);

    Optional<Car> findById(int carId);

    boolean update(Car car);

    boolean delete(int carId, int ownerId);

}
