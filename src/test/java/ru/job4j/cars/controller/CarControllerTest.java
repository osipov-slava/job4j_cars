package ru.job4j.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.Color;
import ru.job4j.cars.model.Type;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.ColorService;
import ru.job4j.cars.service.TypeService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarControllerTest {

    private CarController carController;

    private CarService carService;

    private ColorService colorService;

    private TypeService typeService;

    @BeforeEach
    public void initComponents() {
        carService = mock(CarService.class);
        colorService = mock(ColorService.class);
        typeService = mock(TypeService.class);
        carController = new CarController(carService, colorService, typeService);
    }

    private UserDto initUserDto() {
        UserDto userDto = new UserDto();
        userDto.setTimezone("UTC");
        userDto.setName("User Name");
        userDto.setPassword("password");
        userDto.setEmail("mail@mail.com");
        userDto.setLogin("user");
        return userDto;
    }

    private CarDto initCarDto() {
        CarDto carDto = new CarDto();
        carDto.setId(1);
        carDto.setName("Toyota Corolla");
        carDto.setEngineName("v4 120HP");
        carDto.setColor("Black");
        carDto.setType("Sedan");
        return carDto;
    }

    private List<CarDto> initCarDtos() {
        List<CarDto> carDtos = List.of(initCarDto(), initCarDto());
        carDtos.get(1).setId(2);
        return carDtos;
    }

    private List<Color> initColors() {
        List<Color> colors = List.of(new Color(), new Color());
        colors.get(0).setName("Black");
        colors.get(1).setName("White");
        return colors;
    }

    private List<Type> initTypes() {
        List<Type> types = List.of(new Type(), new Type());
        types.get(0).setName("sedan");
        types.get(1).setName("SUV");
        return types;
    }

    @Test
    public void whenGetAllByUserThenReturnListCarDtos() {
        UserDto userDto = initUserDto();
        var expected = initCarDtos();
        when(carService.findAllByUser(userDto)).thenReturn(expected);

        var model = new ConcurrentModel();
        var view = carController.getAllByUser(model, userDto);
        var actual = model.getAttribute("carDtos");

        assertThat(view).isEqualTo("cars/list");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenGetCreationPageThenReturnColorsTypesCreatePage() {
        var expectedColors = initColors();
        var expectedTypes = initTypes();
        when(colorService.findAll()).thenReturn(expectedColors);
        when(typeService.findAll()).thenReturn(expectedTypes);

        var model = new ConcurrentModel();
        var view = carController.getCreationPage(model);
        var actualColors = model.getAttribute("colors");
        var actualTypes = model.getAttribute("types");

        assertThat(view).isEqualTo("cars/create");
        assertThat(actualColors).usingRecursiveAssertion().isEqualTo(expectedColors);
        assertThat(actualTypes).usingRecursiveAssertion().isEqualTo(expectedTypes);
    }

    @Test
    public void whenPostCreateThenSuccessfulAndRedirectCars() {

    }

    @Test
    public void whenPostCreateThenUnsuccessfulReturnErrorMessage() {

    }
}
