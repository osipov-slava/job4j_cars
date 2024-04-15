package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class HbnHistoryOwnerRepositoryTest {

    @Autowired
    private HbnUserRepository userRepository;

    @Autowired
    private HbnOwnerRepository ownerRepository;

    @Autowired
    private HbnCarRepository carRepository;

    @Autowired
    private HbnHistoryOwnerRepository historyOwnerRepository;

    @AfterEach
    public void clearTables() {
        var ownerHistories = historyOwnerRepository.findAllOrderById();
        for (var priceHistory : ownerHistories) {
            historyOwnerRepository.delete(priceHistory.getId());
        }
        var cars = carRepository.findAllOrderById();
        for (var car : cars) {
            carRepository.delete(car.getId());
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
        userRepository.create(user);

        var owner = new Owner();
        owner.setUser(user);
        owner.setName("Kate");
        ownerRepository.create(owner);
        return owner;
    }

    private Car initCar(Owner owner) {
        var engine = new Engine();
        engine.setName("v4 120HP");
        var car = new Car();
        car.setName("Toyota Corolla");
        car.setEngine(engine);
        car.setOwner(owner);
        carRepository.create(car);
        return car;
    }

    private HistoryOwner initHistoryOwner() {
        var owner = initOwner();
        var car = initCar(owner);
        var historyOwner = new HistoryOwner();
        historyOwner.setOwner(owner);
        historyOwner.setCar(car);
        historyOwner.setStartAt(LocalDateTime.now().minusYears(1));
        historyOwner.setEndAt(LocalDateTime.now());
        historyOwnerRepository.create(historyOwner);
        return historyOwner;
    }

    @Test
    public void whenAddNewHistoryOwnerThenFindSameHistoryOwner() {
        HistoryOwner historyOwner = initHistoryOwner();

        HistoryOwner result = historyOwnerRepository.findById(historyOwner.getId()).get();
        assertThat(result).usingRecursiveAssertion().isEqualTo(historyOwner);
    }

    @Test
    public void whenUpdateHistoryOwnerThenReturnTrue() {
        HistoryOwner historyOwner = initHistoryOwner();

        historyOwner.setStartAt(LocalDateTime.now().minusYears(2));
        historyOwner.setOwner(initOwner2());
        boolean result = historyOwnerRepository.update(historyOwner);
        assertThat(result).isTrue();

        HistoryOwner updated = historyOwnerRepository.findById(historyOwner.getId()).get();
        assertThat(updated).usingRecursiveAssertion().isEqualTo(historyOwner);
    }

    @Test
    public void whenUpdateUnknownHistoryOwnerThenReturnFalse() {
        HistoryOwner historyOwner = new HistoryOwner();
        boolean result = historyOwnerRepository.update(historyOwner);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeleteHistoryOwnerThenReturnTrue() {
        var historyOwner = initHistoryOwner();

        boolean result = historyOwnerRepository.delete(historyOwner.getId());
        assertThat(result).isTrue();

        var optional = historyOwnerRepository.findById(historyOwner.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownHistoryOwnerThenReturnFalse() {
        boolean result = historyOwnerRepository.delete(1);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddOwnerHistoriesThenGetAllOwnerHistories() {
        var historyOwner = initHistoryOwner();

        var historyOwner2 = new HistoryOwner();
        historyOwner2.setOwner(historyOwner.getOwner());
        historyOwner2.setCar(historyOwner.getCar());
        historyOwner2.setStartAt(LocalDateTime.now().minusYears(2));
        historyOwner2.setEndAt(LocalDateTime.now().minusYears(1));
        historyOwnerRepository.create(historyOwner2);

        var expected = Arrays.asList(historyOwner, historyOwner2);
        List<HistoryOwner> actual = historyOwnerRepository.findAllOrderById();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
