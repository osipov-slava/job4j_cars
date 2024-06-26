package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository {

    Owner create(Owner owner);

    List<Owner> findAllOrderById();

    Optional<Owner> findById(Long ownerId);

    Optional<Owner> findByUserId(Long userId);

    boolean update(Owner owner);

    boolean delete(Long ownerId);

}
