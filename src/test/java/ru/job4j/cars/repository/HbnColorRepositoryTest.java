package ru.job4j.cars.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.Color;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HbnColorRepositoryTest {

    @Autowired
    private HbnColorRepository colorRepository;

    @Test
    public void createColorNextDelete() {
        Color color = new Color();
        color.setName("new color");
        colorRepository.create(color);

        var colors = colorRepository.findAll();
        assertThat(colors.contains(color)).isTrue();

        colorRepository.deleteById(color.getId());
        colors = colorRepository.findAll();
        assertThat(colors.contains(color)).isFalse();
    }

}
