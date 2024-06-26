package ru.job4j.cars.repository;

import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PriceHistoryRepository {

    PriceHistory create(PriceHistory priceHistory);

    List<PriceHistory> findAllByPostId(Long postId);

    List<PriceHistory> findAllLastPrice();

    Optional<PriceHistory> findById(Long priceHistoryId);

    Optional<PriceHistory> findLastByPostId(Long postId);

    boolean deleteAllByPostId(Long postId);

}
