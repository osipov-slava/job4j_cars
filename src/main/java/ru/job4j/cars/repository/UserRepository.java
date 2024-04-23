package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    List<User> findAllOrderById();

    Optional<User> findById(int userId);

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean update(User user);

    boolean delete(int userId);
}
