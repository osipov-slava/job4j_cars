package ru.job4j.cars.service;

import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDto create(PostDto task, CarDto carDto, List<File> files);

    Optional<PostDto> findById(Long id, UserDto userDto);

    List<PostDto> findAll(UserDto userDto);

    boolean update(PostDto postDto, UserDto userDto);

    boolean deleteById(Long id, UserDto userDto);

    List<PostDto> findActive(UserDto userDto);

    List<PostDto> findInactive(UserDto userDto);

    List<PostDto> findWithPhotos(UserDto userDto);

    List<PostDto> findLastDay(UserDto userDto);

}
