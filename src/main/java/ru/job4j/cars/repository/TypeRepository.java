package ru.job4j.cars.repository;

import ru.job4j.cars.model.Type;

import java.util.List;

public interface TypeRepository {

    Type create(Type type);

    List<Type> findAll();

    boolean deleteById(int id);

}