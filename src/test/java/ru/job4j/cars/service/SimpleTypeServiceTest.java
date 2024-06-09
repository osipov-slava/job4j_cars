package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Type;
import ru.job4j.cars.repository.TypeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleTypeServiceTest {

    private TypeRepository typeRepository;

    private TypeService typeService;

    @BeforeEach
    public void initComponents() {
        typeRepository = mock(TypeRepository.class);
        typeService = new SimpleTypeService(typeRepository);
    }

    private Type initType() {
        Type type = new Type();
        type.setId(1);
        type.setName("Sedan");
        return type;
    }

    private List<Type> initListTypes() {
        Type firstType = initType();
        Type secondType = new Type();
        secondType.setId(2);
        secondType.setName("SUV");
        return List.of(firstType, secondType);
    }

    @Test
    public void whenCreateType() {
        var type = initType();
        when(typeRepository.create(type)).thenReturn(type);
        var actual = typeService.create(type);

        assertThat(actual).usingRecursiveAssertion().isEqualTo(type);
    }

    @Test
    public void whenFindAllTypes() {
        var expected = initListTypes();
        when(typeRepository.findAll()).thenReturn(expected);
        var actual = typeService.findAll();

        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenDeleteTypeThenReturnTrue() {
        when(typeRepository.deleteById(1)).thenReturn(true);
        var actual = typeService.deleteById(1);

        assertThat(actual).isTrue();
    }

    @Test
    public void whenDeleteUnknownTypeThenReturnFalse() {
        when(typeRepository.deleteById(anyInt())).thenReturn(false);
        var actual = typeService.deleteById(1);

        assertThat(actual).isFalse();
    }

}
