package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.repository.ColorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SimpleColorServiceTest {

    private ColorRepository colorRepository;

    private ColorService colorService;

    @BeforeEach
    public void initComponents() {
        colorRepository = mock(ColorRepository.class);
        colorService = new SimpleColorService(colorRepository);
    }

    private Color initColor() {
        Color color = new Color();
        color.setId(1);
        color.setName("Black");
        return color;
    }

    private List<Color> initListColors() {
        Color firstColor = initColor();
        Color secondColor = new Color();
        secondColor.setId(2);
        secondColor.setName("White");
        return List.of(firstColor, secondColor);
    }

    @Test
    public void whenCreateColor() {
        var color = initColor();
        when(colorRepository.create(color)).thenReturn(color);
        var actual = colorService.create(color);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(color);
    }

    @Test
    public void whenFindAllColors() {
        var expected = initListColors();
        when(colorRepository.findAll()).thenReturn(expected);
        var actual = colorService.findAll();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenDeleteColorThenReturnTrue() {
        when(colorRepository.deleteById(1)).thenReturn(true);
        var actual = colorService.deleteById(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenDeleteUnknownColorThenReturnFalse() {
        when(colorRepository.deleteById(anyInt())).thenReturn(false);
        var actual = colorService.deleteById(1);
        assertThat(actual).isFalse();
    }

}
