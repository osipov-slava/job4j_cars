package ru.job4j.cars.service;

import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PriceHistoryService {

    PriceHistory create(Post post, long price);

    Optional<PriceHistory> findById(int id);

    Optional<PriceHistory> findLastByPostId(int postId);

    List<PriceHistory> findAll();

    List<PriceHistory> findAllByPostId(int postId);

    List<PriceHistory> findAllLastPrice();

    boolean update(PostDto postDto, long price);

    boolean deleteById(int id);

    boolean deleteAllByPostId(int postId);

}
