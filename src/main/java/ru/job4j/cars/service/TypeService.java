package ru.job4j.cars.service;

import ru.job4j.cars.model.Type;

import java.util.List;

public interface TypeService {

    Type create(Type type);

    List<Type> findAll();

    boolean deleteById(Integer id);

}
