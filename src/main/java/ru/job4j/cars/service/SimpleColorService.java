package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.ColorRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SimpleColorService implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public Color create(Color color) {
        return colorRepository.create(color);
    }

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public boolean deleteById(int id) {
        return colorRepository.deleteById(id);
    }

}
