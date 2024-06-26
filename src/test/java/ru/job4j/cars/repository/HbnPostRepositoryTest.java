package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HbnPostRepositoryTest {

    @Autowired
    private HbnUserRepository userRepository;

    @Autowired
    private HbnOwnerRepository ownerRepository;

    @Autowired
    private HbnCarRepository carRepository;

    @Autowired
    private HbnPostRepository postRepository;

    @Autowired
    private HbnFileRepository fileRepository;

    @Autowired
    private HbnColorRepository colorRepository;

    @Autowired
    private HbnTypeRepository typeRepository;

    @AfterEach
    public void clearTables() {
        var files = fileRepository.findAllOrderById();
        for (var file : files) {
            fileRepository.delete(file.getId());
        }
        var posts = postRepository.findAllOrderById();
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

    private User initUser() {
        var user = new User();
        user.setLogin("user");
        user.setPassword("password");
        user.setEmail("some@gmail.com");
        userRepository.create(user);
        return user;
    }

    private Car initCar(User user) {
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
        return car;
    }

    private Post initPostNoCommit() {
        var user = initUser();
        var car = initCar(user);
        var post = new Post();
        post.setDescription("post");
        post.setCreated(LocalDateTime.now());
        post.setUser(user);
        post.setCar(car);
        post.setIsActive(true);
        return post;
    }

    private Post initPost2NoCommit(Post post) {
        var post2 = new Post();
        post2.setDescription("post2");
        post2.setCreated(LocalDateTime.now());
        post2.setUser(post.getUser());
        post2.setCar(post.getCar());
        post2.setIsActive(false);
        return post2;
    }

    @Test
    public void whenAddNewPostThenFindSamePost() {
        var post = initPostNoCommit();
        var files = List.of(
                new File(null, "directory/", "filename1", post),
                new File(null, "directory/", "filename2", post));
        post.setFiles(files);
        postRepository.create(post);

        Post result = postRepository.findById(post.getId()).get();
        assertThat(result).usingRecursiveAssertion().isEqualTo(post);
    }

    @Test
    public void whenUpdatePostThenReturnTrue() {
        var post = initPostNoCommit();
        postRepository.create(post);

        post.setDescription("post2");
        boolean result = postRepository.update(post, post.getUser());
        assertThat(result).isTrue();

        Post updated = postRepository.findById(post.getId()).get();
        assertThat(updated).usingRecursiveAssertion().isEqualTo(post);
    }

    @Test
    public void whenUpdateUnknownPostThenReturnFalse() {
        var user = initUser();
        Post post = new Post();
        boolean result = postRepository.update(post, user);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeletePostThenReturnTrue() {
        var post = initPostNoCommit();
        postRepository.create(post);

        boolean result = postRepository.delete(post.getId(), post.getUser());
        assertThat(result).isTrue();

        var optional = postRepository.findById(post.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownPostThenReturnFalse() {
        var user = initUser();
        boolean result = postRepository.delete(1L, user);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddPostsThenGetAllPosts() {
        var post = initPostNoCommit();
        postRepository.create(post);

        var post2 = initPost2NoCommit(post);
        postRepository.create(post2);

        var expected = Arrays.asList(post2, post);
        List<Post> actual = postRepository.findAllOrderById();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenAddPostsThenGetActive() {
        var post = initPostNoCommit();
        postRepository.create(post);

        var post2 = initPost2NoCommit(post);
        postRepository.create(post2);

        var expected = Arrays.asList(post);
        List<Post> actual = postRepository.findActive();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenAddPostsThenGetInactive() {
        var post = initPostNoCommit();
        postRepository.create(post);

        var post2 = initPost2NoCommit(post);
        postRepository.create(post2);

        var expected = Arrays.asList(post2);
        List<Post> actual = postRepository.findInactive();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFindAllForLastDay() {
        var post = initPostNoCommit();
        postRepository.create(post);

        var post2 = initPost2NoCommit(post);
        post2.setCreated(LocalDateTime.now().minusDays(1).minusMinutes(10));
        postRepository.create(post2);

        var expected = List.of(post);
        List<Post> actual = postRepository.findAllForLastDay();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void testFindAllPostsWithCarModelLike() {
        var post = initPostNoCommit();
        postRepository.create(post);

        var post2 = initPost2NoCommit(post);
        postRepository.create(post2);

        var expected = Arrays.asList(post, post2);
        List<Post> actual = postRepository.findAllCarModelLike("Corol");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);

        expected = List.of();
        actual = postRepository.findAllCarModelLike("unknown");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void testFindAllWithFile() {
        var post = initPostNoCommit();
        postRepository.create(post);

        var post2 = initPost2NoCommit(post);
        var files = List.of(
                new File(null, "directory/", "filename1", post2),
                new File(null, "directory/", "filename2", post2));
        post2.setFiles(files);
        postRepository.create(post2);

        List<Post> expected = List.of(post2);
        List<Post> actual = postRepository.findAllWithFile();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void testPostsWithSubscribersByPosts() {
        var post = initPostNoCommit();
        postRepository.create(post);

        var post2 = initPost2NoCommit(post);
        post2.setSubscribers(List.of(post.getUser()));
        postRepository.create(post2);

        var expected = Arrays.asList(post, post2);
        var postsWithoutSubscribers = postRepository.findAllOrderById();
        var actual = postRepository.findPostsWithSubscribersByPosts(postsWithoutSubscribers);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
