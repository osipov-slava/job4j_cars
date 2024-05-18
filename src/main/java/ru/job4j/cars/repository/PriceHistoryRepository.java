package ru.job4j.cars.repository;

import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PriceHistoryRepository {

    PriceHistory create(PriceHistory priceHistory);

    List<PriceHistory> findAllByPostId(int postId);

    List<PriceHistory> findAllLastPrice();

    Optional<PriceHistory> findById(int priceHistoryId);

    Optional<PriceHistory> findLastByPostId(int postId);

    boolean deleteAllByPostId(int postId);

}
