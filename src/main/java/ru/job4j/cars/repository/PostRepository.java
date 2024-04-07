package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post create(Post post);

    List<Post> findAllOrderById();

    Optional<Post> findById(int postId);

    boolean update(Post post);

    boolean delete(int postId);
}
