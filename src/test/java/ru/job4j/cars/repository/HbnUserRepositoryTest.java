package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HbnUserRepositoryTest {

    @Autowired
    private HbnUserRepository userRepository;

    @AfterEach
    public void clearUsers() {
        var users = userRepository.findAllOrderById();
        for (var user : users) {
            userRepository.delete(user.getId());
        }
    }

    private User initUser() {
        User user = new User();
        user.setLogin("user1");
        user.setPassword("password1");
        user.setEmail("some@gmail.com");
        userRepository.create(user);
        return user;
    }

    private User initUser2() {
        User user = new User();
        user.setLogin("user2");
        user.setPassword("password2");
        user.setEmail("other@gmail.com");
        userRepository.create(user);
        return user;
    }

    @Test
    public void whenAddNewUserThenFindSameUser() {
        User user = initUser();

        User result = userRepository.findById(user.getId()).get();
        assertThat(result).usingRecursiveAssertion().isEqualTo(user);
    }

    @Test
    public void whenUpdateUserThenReturnTrue() {
        User user = initUser();

        user.setLogin("user2");
        user.setPassword("password2");
        boolean result = userRepository.update(user);
        assertThat(result).isTrue();

        User updated = userRepository.findById(user.getId()).get();
        assertThat(updated).usingRecursiveAssertion().isEqualTo(user);
    }

    @Test
    public void whenUpdateUnknownUserThenReturnFalse() {
        var user = new User();
        boolean result = userRepository.update(user);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeleteUserThenReturnTrue() {
        User user = initUser();

        boolean result = userRepository.delete(user.getId());
        assertThat(result).isTrue();

        var optional = userRepository.findById(user.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownUserThenReturnFalse() {
        boolean result = userRepository.delete(1);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddUsersThenGetAllUsers() {
        User user = initUser();
        User user2 = initUser2();

        var expected = Arrays.asList(user, user2);
        List<User> actual = userRepository.findAllOrderById();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
