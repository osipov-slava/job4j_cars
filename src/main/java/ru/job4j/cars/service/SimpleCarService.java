package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CarRepository;


import java.util.List;
import java.util.Optional;

//@Service
//@AllArgsConstructor
//public class SimpleCarService implements CarService {
//
//    private final CarRepository carRepository;
//
//    @Override
//    public Car create(Car car, User user, List<Integer> categoryIds) {
//        car.setUser(user);
//        car.setCategories(categoryService.findByListIds(categoryIds));
//        return carRepository.create(car);
//    }
//
//    @Override
//    public boolean update(int id, Car car, List<Integer> categoryIds) {
//        categoryIds.forEach(c -> car.getCategories().add(new Category(c, null)));
//        return carRepository.update(car);
//    }
//
//    @Override
//    public boolean deleteById(int id, User user) {
//        return carRepository.deleteById(id, user);
//    }
//
//    @Override
//    public Optional<Car> findById(int id, User user) {
//        return carRepository.findById(id, user);
//    }
//
//    @Override
//    public List<Car> findAll(User user) {
//        return carRepository.findAllOrderById(user);
//    }
//
//}
