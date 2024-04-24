package ru.job4j.cars.service;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post create(Post task, UserDto userDto);

//    boolean update(int id, Post task, List<Integer> categoryIds);

//    boolean done(int id, UserDto userDto);

//    boolean deleteById(int id, UserDto userDto);

    Optional<Post> findById(int id);

    List<Post> findAll();

//    List<Post> findFinished(UserDto userDto);
//
//    List<Post> findInProgress(UserDto userDto);
}
