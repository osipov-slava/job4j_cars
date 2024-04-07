package ru.job4j.cars.repository;

import ru.job4j.cars.model.HistoryOwner;

import java.util.List;
import java.util.Optional;

public interface HistoryOwnerRepository {

    HistoryOwner create(HistoryOwner historyOwner);

    List<HistoryOwner> findAllOrderById();

    Optional<HistoryOwner> findById(int historyOwnerId);

    boolean update(HistoryOwner historyOwner);

    boolean delete(int historyOwnerId);
}
