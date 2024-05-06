package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post create(Post post);

    List<Post> findAllOrderById();

    Optional<Post> findById(int postId);

    List<Post> findPostsWithSubscribersByPosts(List<Post> posts);

    List<Post> findAllForLastDay();

    List<Post> findAllWithFile();

    List<Post> findAllCarModelLike(String key);

    boolean update(Post post, User user);

    boolean delete(int postId, User user);
}
