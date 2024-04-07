package ru.job4j.cars.repository;

import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PriceHistoryRepository {

    PriceHistory create(PriceHistory priceHistory);

    List<PriceHistory> findAllOrderById();

    Optional<PriceHistory> findById(int priceHistoryId);

    boolean update(PriceHistory priceHistory);

    boolean delete(int priceHistoryId);
}
