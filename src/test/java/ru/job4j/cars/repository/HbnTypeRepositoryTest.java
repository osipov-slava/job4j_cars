package ru.job4j.cars.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.Type;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HbnTypeRepositoryTest {

    @Autowired
    TypeRepository typeRepository;

    @Test
    public void createTypeNextDelete() {
        Type type = new Type();
        type.setName("new type");
        typeRepository.create(type);

        var types = typeRepository.findAll();
        assertThat(types.contains(type)).isTrue();

        typeRepository.deleteById(type.getId());
        types = typeRepository.findAll();
        assertThat(types.contains(type)).isFalse();
    }

}
