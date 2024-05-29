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

    @Autowired
    private HbnColorRepository colorRepository;

    @Autowired
    private HbnTypeRepository typeRepository;

    @AfterEach
    public void clearTables() {
        var posts = postRepository.findAllOrderById();
        for (var post : posts) {
            priceHistoryRepository.deleteAllByPostId(post.getId());
        }
        for (var post : posts) {
            postRepository.delete(post.getId(), post.getUser());
        }
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

    private Post initPost() {
        var user = new User();
        user.setLogin("user");
        user.setPassword("password");
        user.setEmail("some@gmail.com");
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
        List<Type> types = typeRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        car.setType(types.get(0));
        car.setColor(colors.get(0));
        carRepository.create(car);

        var post = new Post();
        post.setDescription("post");
        post.setCreated(LocalDateTime.now());
        post.setCar(car);
        post.setUser(user);
        post.setIsActive(true);
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
    public void whenDeletePriceHistoriesThenReturnTrue() {
        PriceHistory priceHistory = initPriceHistory();

        var priceHistory2 = new PriceHistory();
        priceHistory2.setPost(priceHistory.getPost());
        priceHistory2.setBefore(14000);
        priceHistory2.setAfter(13000);
        priceHistoryRepository.create(priceHistory2);

        boolean result = priceHistoryRepository.deleteAllByPostId(priceHistory.getPost().getId());
        assertThat(result).isTrue();

        List<PriceHistory> listPH = priceHistoryRepository.findAllByPostId(priceHistory.getPost().getId());
        assertThat(listPH.isEmpty()).isTrue();
    }

    @Test
    public void whenAddPriceHistoriesThenGetAllPriceHistoriesByPostId() {
        PriceHistory priceHistory = initPriceHistory();

        var priceHistory2 = new PriceHistory();
        priceHistory2.setPost(priceHistory.getPost());
        priceHistory2.setBefore(14000);
        priceHistory2.setAfter(13000);
        priceHistoryRepository.create(priceHistory2);

        var expected = Arrays.asList(priceHistory, priceHistory2);
        List<PriceHistory> actual = priceHistoryRepository.findAllByPostId(priceHistory.getPost().getId());
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
