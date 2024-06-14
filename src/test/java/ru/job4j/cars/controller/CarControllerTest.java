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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
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
        userDto.setId(1);
        userDto.setTimezone("UTC");
        userDto.setName("User Name");
        userDto.setPassword("password");
        userDto.setEmail("mail@mail.com");
        userDto.setLogin("user");
        userDto.setOwnerId(2);
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
        var userDto = initUserDto();
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
        var userDto = initUserDto();
        var carDto = initCarDto();
        var expected = initCarDto();
        expected.setOwnerId(userDto.getOwnerId());
        when(carService.create(carDto)).thenReturn(carDto);

        var model = new ConcurrentModel();
        var view = carController.create(carDto, model, userDto);

        assertThat(view).isEqualTo("redirect:/cars");
        assertThat(carDto).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenPostCreateThenUnsuccessfulReturnErrorMessage() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        carDto.setId(0);
        when(carService.create(carDto)).thenReturn(carDto);

        var model = new ConcurrentModel();
        var view = carController.create(carDto, model, userDto);
        var actualMessage = model.getAttribute("message");
        var expectedMessage = "Creation carDto was unsuccessful!";

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenGetByIdThenReturnCarDto() {
        var expected = initCarDto();
        when(carService.findById(2)).thenReturn(Optional.of(expected));

        var model = new ConcurrentModel();
        var view = carController.getById(model, 2);
        var actual = model.getAttribute("carDto");

        assertThat(view).isEqualTo("cars/one");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenGetByWrongIdThenReturnErrorMessage() {
        when(carService.findById(anyInt())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = carController.getById(model, 2);
        var expectedMessage = "CarDto with this Id not found!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenGetEditByIdThenReturnCarDtoColorsTypesEditPage() {
        var expectedCarDto = initCarDto();
        var expectedColors = initColors();
        var expectedTypes = initTypes();

        when(colorService.findAll()).thenReturn(expectedColors);
        when(typeService.findAll()).thenReturn(expectedTypes);
        when(carService.findById(2)).thenReturn(Optional.of(expectedCarDto));

        var model = new ConcurrentModel();
        var view = carController.editById(model, 2);
        var actualCarDto = model.getAttribute("carDto");
        var actualColors = model.getAttribute("colors");
        var actualTypes = model.getAttribute("types");

        assertThat(view).isEqualTo("cars/edit");
        assertThat(actualCarDto).usingRecursiveAssertion().isEqualTo(expectedCarDto);
        assertThat(actualColors).usingRecursiveAssertion().isEqualTo(expectedColors);
        assertThat(actualTypes).usingRecursiveAssertion().isEqualTo(expectedTypes);
    }

    @Test
    public void whenGetEditByWrongIdThenReturnErrorMessage() {
        when(carService.findById(anyInt())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = carController.editById(model, 2);
        var expectedMessage = "CarDto with this Id not found!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenPostUpdateThenRedirectCars() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        when(carService.update(carDto, userDto)).thenReturn(true);

        var model = new ConcurrentModel();
        var view = carController.update(carDto, userDto, model);

        assertThat(view).isEqualTo("redirect:/cars");
    }

    @Test
    public void whenPostUpdateThenReturnErrorMessage() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        when(carService.update(carDto, userDto)).thenReturn(false);

        var model = new ConcurrentModel();
        var view = carController.update(carDto, userDto, model);
        var expectedMessage = "Update carDto was unsuccessful!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenGetDeleteThenRedirectCars() {
        var userDto = initUserDto();
        when(carService.deleteById(2, userDto)).thenReturn(true);

        var model = new ConcurrentModel();
        var view = carController.delete(model, 2, userDto);

        assertThat(view).isEqualTo("redirect:/cars");
    }

    @Test
    public void whenGetDeleteThenReturnErrorMessage() {
        var userDto = initUserDto();
        when(carService.deleteById(2, userDto)).thenReturn(false);

        var model = new ConcurrentModel();
        var view = carController.delete(model, 2, userDto);
        var expectedMessage = "CarDto with this Id not found!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}