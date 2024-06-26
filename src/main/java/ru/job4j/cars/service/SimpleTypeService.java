package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Type;
import ru.job4j.cars.repository.TypeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SimpleTypeService implements TypeService {

    private final TypeRepository typeRepository;

    @Override
    public Type create(Type type) {
        return typeRepository.create(type);
    }

    @Override
    public List<Type> findAll() {
        return typeRepository.findAll();
    }

    @Override
    public boolean deleteById(Integer id) {
        return typeRepository.deleteById(id);
    }

}
