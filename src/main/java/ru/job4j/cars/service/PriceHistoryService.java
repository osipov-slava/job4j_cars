package ru.job4j.cars.service;

import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.PriceHistoryDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PriceHistoryService {

    PriceHistory create(Post post, long price);

    Optional<PriceHistory> findById(Long id);

    Optional<PriceHistory> findLastByPostId(Long postId);

    List<PriceHistoryDto> findAllByPostId(Long postId, UserDto userDto);

    List<PriceHistory> findAllLastPrice();

    boolean update(PostDto postDto, long price);

    boolean deleteAllByPostId(Long postId);

}
