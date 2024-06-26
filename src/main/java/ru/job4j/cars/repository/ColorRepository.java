package ru.job4j.cars.repository;


import ru.job4j.cars.model.Color;

import java.util.List;

public interface ColorRepository {

    Color create(Color color);

    List<Color> findAll();

    boolean deleteById(Integer id);

}
