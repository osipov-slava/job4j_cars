package ru.job4j.cars.service;

import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDto create(PostDto task, UserDto userDto, CarDto carDto);

    Optional<PostDto> findById(int id);

    List<PostDto> findAll();

    boolean update(PostDto postDto, UserDto userDto);

//    boolean done(int id, UserDto userDto);

    boolean deleteById(int id, UserDto userDto);

//    List<Post> findFinished(UserDto userDto);
//
//    List<Post> findInProgress(UserDto userDto);
}
