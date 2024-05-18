package ru.job4j.cars.service;

import ru.job4j.cars.model.Color;

import java.util.List;

public interface ColorService {

    Color create(Color color);

    List<Color> findAll();

    boolean deleteById(int id);

}
