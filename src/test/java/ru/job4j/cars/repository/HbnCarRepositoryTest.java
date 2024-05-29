package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HbnCarRepositoryTest {

    @Autowired
    private HbnUserRepository userRepository;

    @Autowired
    private HbnOwnerRepository ownerRepository;

    @Autowired
    private HbnCarRepository carRepository;

    @Autowired
    private HbnColorRepository colorRepository;

    @Autowired
    private HbnTypeRepository typeRepository;

    @AfterEach
    public void clearTables() {
        var cars = carRepository.findAll();
        for (var car : cars) {
            carRepository.delete(car.getId(), car.getOwner().getId());
        }
        var owners = ownerRepository.findAllOrderById();
        for (var owner : owners) {
            ownerRepository.delete(owner.getId());
        }
        var users = userRepository.findAllOrderById();
        for (var user : users) {
            userRepository.delete(user.getId());
        }
    }

    private Owner initOwner() {
        var user = new User();
        user.setLogin("user");
        user.setPassword("password");
        user.setEmail("some@gmail.com");
        userRepository.create(user);

        var owner = new Owner();
        owner.setUser(user);
        owner.setName("John");
        ownerRepository.create(owner);
        return owner;
    }

    private Car initCar() {
        var engine = new Engine();
        engine.setName("v4 120HP");

        var car = new Car();
        car.setName("Toyota Corolla");
        car.setEngine(engine);
        car.setOwner(initOwner());
        List<Type> types = typeRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        car.setType(types.get(0));
        car.setColor(colors.get(0));
        carRepository.create(car);
        return car;
    }

    @Test
    public void whenAddNewCarThenFindSameCar() {
        Car car = initCar();

        Car result = carRepository.findById(car.getId()).get();
        assertThat(result).usingRecursiveAssertion().isEqualTo(car);
    }

    @Test
    public void whenUpdateCarThenReturnTrue() {
        Car car = initCar();

        car.setName("Other car");
        boolean result = carRepository.update(car);
        assertThat(result).isTrue();

        Car updated = carRepository.findById(car.getId()).get();
        assertThat(updated).usingRecursiveAssertion().isEqualTo(car);
    }

    @Test
    public void whenUpdateUnknownCarThenReturnFalse() {
        Car car = new Car();
        boolean result = carRepository.update(car);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeleteCarThenReturnTrue() {
        Car car = initCar();

        boolean result = carRepository.delete(car.getId(), car.getOwner().getId());
        assertThat(result).isTrue();

        var optional = carRepository.findById(car.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownCarThenReturnFalse() {
        boolean result = carRepository.delete(1, 1);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddCarsThenGetAllCars() {
        Car car = initCar();

        var engine2 = new Engine();
        engine2.setName("v4 150HP");
        var car2 = new Car();
        car2.setName("Toyota Prius");
        car2.setEngine(engine2);
        car2.setOwner(car.getOwner());
        List<Type> types = typeRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        car2.setType(types.get(1));
        car2.setColor(colors.get(2));
        carRepository.create(car2);

        var expected = Arrays.asList(car, car2);
        List<Car> actual = carRepository.findAll();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
