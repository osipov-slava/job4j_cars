package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private Owner initOwner2() {
        var user = new User();
        user.setLogin("user2");
        user.setPassword("password");
        user.setEmail("other@gmail.com");
        userRepository.create(user);

        var owner = new Owner();
        owner.setUser(user);
        owner.setName("Kate");
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

    private Car initCar2WithSameOwner(Owner owner) {
        var engine = new Engine();
        engine.setName("Hybrid");

        var car = new Car();
        car.setName("Toyota Prius");
        car.setEngine(engine);
        car.setOwner(owner);
        List<Type> types = typeRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        car.setType(types.get(1));
        car.setColor(colors.get(1));
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
    public void whenUpdateUnknownCarThenThrowRuntimeException() {
        Car car = new Car();
        car.setId(5L);

        assertThrows(RuntimeException.class, () -> {
            carRepository.update(car);
        });
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
        boolean result = carRepository.delete(1L, 1L);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddCarsThenGetAllCars() {
        Car car = initCar();

        Car car2 = initCar2WithSameOwner(car.getOwner());

        var expected = Arrays.asList(car, car2);
        List<Car> actual = carRepository.findAll();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenFindAllByOwner() {
        Car car = initCar();

        Car car2 = initCar2WithSameOwner(car.getOwner());

        var engine3 = new Engine();
        engine3.setName("v6");
        var car3 = new Car();
        car3.setName("Toyota Crown");
        car3.setEngine(engine3);
        car3.setOwner(initOwner2());
        List<Type> types = typeRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        car3.setType(types.get(1));
        car3.setColor(colors.get(2));
        carRepository.create(car3);

        var expected = Arrays.asList(car, car2);
        List<Car> actual = carRepository.findAllByOwnerId(car.getOwner().getId());
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
