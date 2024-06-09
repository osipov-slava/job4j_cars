package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.CarMapper;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.EngineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SimpleCarServiceTest {

    private CarRepository carRepository;

    private EngineRepository engineRepository;

    @Autowired
    private CarMapper carMapper;

    private CarService carService;

    @BeforeEach
    public void initComponents() {
        carRepository = mock(CarRepository.class);
        engineRepository = mock(EngineRepository.class);
        carService = new SimpleCarService(carRepository, engineRepository, carMapper);
    }

    private Car initCar() {
        var engine = new Engine();
        engine.setName("v4 120HP");

        Type type = new Type();
        type.setId(1);
        type.setName("Sedan");

        Color color = new Color();
        color.setId(1);
        color.setName("Black");

        var car = new Car();
        car.setId(1);
        car.setName("Toyota Corolla");
        car.setEngine(engine);
        car.setOwner(new Owner());
        car.getOwner().setId(100);
        car.setColor(color);
        car.setType(type);
        return car;
    }

    private List<Car> initCars() {
        List<Car> cars = new ArrayList<>();
        var car = initCar();
        cars.add(car);
        car = initCar();
        car.setId(2);
        cars.add(car);
        return cars;
    }

    private List<CarDto> initCarDtos(List<Car> cars) {
        List<CarDto> carDtos = new ArrayList<>();
        for (Car car : cars) {
            carDtos.add(carMapper.getModelFromEntity(car));
        }
        return carDtos;
    }

    @Test
    public void whenCreate() {
        Car car = initCar();
        CarDto carDto = carMapper.getModelFromEntity(car);
        when(carRepository.create(car)).thenReturn(car);

        var actual = carService.create(carDto);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(carDto);
    }

    @Test
    public void whenFindByIdThenSuccessful() {
        Car car = initCar();
        CarDto carDto = carMapper.getModelFromEntity(car);
        when(carRepository.findById(1)).thenReturn(Optional.of(car));

        var actual = carService.findById(1);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveAssertion().isEqualTo(carDto);
    }

    @Test
    public void whenFindByIdThenUnsuccessful() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        var actual = carService.findById(1);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    public void whenFindAll() {
        List<Car> cars = initCars();
        List<CarDto> carDtos = initCarDtos(cars);

        when(carRepository.findAll()).thenReturn(cars);

        var actual = carService.findAll();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(carDtos);
    }

    @Test
    public void whenFindAllByUserThenReturnListCarDtos() {
        List<Car> cars = initCars();
        List<CarDto> carDtos = initCarDtos(cars);

        UserDto userDto = new UserDto();
        userDto.setOwnerId(cars.get(0).getOwner().getId());

        when(carRepository.findAllByOwnerId(userDto.getOwnerId())).thenReturn(cars);

        var actual = carService.findAllByUser(userDto);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(carDtos);
    }

    @Test
    public void whenUpdateValidCarAndUserThenSuccessful() {
        Car carOld = initCar();
        Car carNew = initCar();
        carNew.setName("Ford");

        Type typeNew = new Type();
        typeNew.setId(2);
        typeNew.setName("Coupe");
        carNew.setType(typeNew);

        Color colorNew = new Color();
        colorNew.setId(2);
        colorNew.setName("White");
        carNew.setColor(colorNew);
        CarDto carDtoNew = carMapper.getModelFromEntity(carNew);

        UserDto userDto = new UserDto();
        userDto.setOwnerId(carOld.getOwner().getId());

        when(carRepository.findById(carOld.getId())).thenReturn(Optional.of(carOld));
        when(engineRepository.update(carNew.getEngine())).thenReturn(true);
        when(carRepository.update(carNew)).thenReturn(true);

        var actual = carService.update(carDtoNew, userDto);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenUpdateInvalidCarThenUnsuccessful() {
        Car carOld = initCar();
        Car carNew = initCar();
        carNew.setId(carOld.getId() + 1);

        CarDto carDtoNew = carMapper.getModelFromEntity(carNew);

        UserDto userDto = new UserDto();
        userDto.setOwnerId(carOld.getOwner().getId());

        when(carRepository.findById(carOld.getId())).thenReturn(Optional.empty());

        var actual = carService.update(carDtoNew, userDto);
        assertThat(actual).isFalse();
    }

    @Test
    public void whenUpdateInvalidUserThenUnsuccessful() {
        Car carOld = initCar();
        Car carNew = initCar();
        carNew.setName("Ford");
        CarDto carDtoNew = carMapper.getModelFromEntity(carNew);

        UserDto userDto = new UserDto();
        userDto.setOwnerId(carOld.getOwner().getId() + 1);

        when(carRepository.findById(carOld.getId())).thenReturn(Optional.of(carOld));
        when(carRepository.update(carNew)).thenReturn(true);

        var actual = carService.update(carDtoNew, userDto);
        assertThat(actual).isFalse();
    }

    @Test
    public void whenDeleteColorThenReturnTrue() {
        UserDto userDto = new UserDto();
        userDto.setOwnerId(1);
        when(carRepository.delete(1, userDto.getOwnerId())).thenReturn(true);

        var actual = carService.deleteById(1, userDto);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenDeleteUnknownColorThenReturnFalse() {
        UserDto userDto = new UserDto();
        userDto.setOwnerId(1);
        when(carRepository.delete(anyInt(), anyInt())).thenReturn(false);

        var actual = carService.deleteById(1, userDto);
        assertThat(actual).isFalse();
    }

}
