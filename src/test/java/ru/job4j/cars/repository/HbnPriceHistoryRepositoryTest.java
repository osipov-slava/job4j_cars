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
public class HbnPriceHistoryRepositoryTest {

    @Autowired
    private HbnUserRepository userRepository;

    @Autowired
    private HbnOwnerRepository ownerRepository;

    @Autowired
    private HbnCarRepository carRepository;

    @Autowired
    private HbnPriceHistoryRepository priceHistoryRepository;

    @Autowired
    private HbnPostRepository postRepository;

    @AfterEach
    public void clearTables() {
        var priceHistories = priceHistoryRepository.findAllOrderById();
        for (var priceHistory : priceHistories) {
            priceHistoryRepository.delete(priceHistory.getId());
        }
        var posts = postRepository.findAllOrderById();
        for (var post : posts) {
            postRepository.delete(post.getId());
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

    private Post initPost() {
        var user = new User();
        user.setLogin("user");
        user.setPassword("password");
        userRepository.create(user);

        var owner = new Owner();
        owner.setUser(user);
        owner.setName("John");
        ownerRepository.create(owner);

        var engine = new Engine();
        engine.setName("v4 120HP");
        var car = new Car();
        car.setName("Toyota Corolla");
        car.setEngine(engine);
        car.setOwner(owner);
        carRepository.create(car);

        var post = new Post();
        post.setDescription("post");
        post.setCreated(LocalDateTime.now());
        post.setCar(car);
        post.setUser(user);
        postRepository.create(post);
        return post;
    }

    private PriceHistory initPriceHistory() {
        var post = initPost();
        var priceHistory = new PriceHistory();
        priceHistory.setPost(post);
        priceHistory.setBefore(15000);
        priceHistory.setAfter(14000);
        priceHistoryRepository.create(priceHistory);
        return priceHistory;
    }

    @Test
    public void whenAddNewPriceHistoryThenFindSamePriceHistory() {
        PriceHistory priceHistory = initPriceHistory();

        PriceHistory result = priceHistoryRepository.findById(priceHistory.getId()).get();
        assertThat(result).usingRecursiveAssertion().isEqualTo(priceHistory);
    }

    @Test
    public void whenUpdatePriceHistoryThenReturnTrue() {
        PriceHistory priceHistory = initPriceHistory();

        priceHistory.setAfter(13500);
        boolean result = priceHistoryRepository.update(priceHistory);
        assertThat(result).isTrue();

        PriceHistory updated = priceHistoryRepository.findById(priceHistory.getId()).get();
        assertThat(updated).usingRecursiveAssertion().isEqualTo(priceHistory);
    }

    @Test
    public void whenUpdateUnknownPriceHistoryThenReturnFalse() {
        var priceHistory = new PriceHistory();
        boolean result = priceHistoryRepository.update(priceHistory);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeletePriceHistoryThenReturnTrue() {
        PriceHistory priceHistory = initPriceHistory();

        boolean result = priceHistoryRepository.delete(priceHistory.getId());
        assertThat(result).isTrue();

        var optional = priceHistoryRepository.findById(priceHistory.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownPriceHistoryThenReturnFalse() {
        boolean result = priceHistoryRepository.delete(1);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddPriceHistoriesThenGetAllPriceHistories() {
        PriceHistory priceHistory = initPriceHistory();

        var priceHistory2 = new PriceHistory();
        priceHistory2.setPost(priceHistory.getPost());
        priceHistory2.setBefore(14000);
        priceHistory2.setAfter(13000);
        priceHistoryRepository.create(priceHistory2);

        var expected = Arrays.asList(priceHistory, priceHistory2);
        List<PriceHistory> actual = priceHistoryRepository.findAllOrderById();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
