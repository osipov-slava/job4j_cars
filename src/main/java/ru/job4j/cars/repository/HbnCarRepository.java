package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnCarRepository implements CarRepository {

    private final CrudRepository crudRepository;

    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public List<Car> findAll() {
        return crudRepository.query("""
                from Car c
                join fetch c.engine
                join fetch c.owner
                left join fetch c.historyOwners
                order by c.name asc""", Car.class);
    }

    public List<Car> findAllByOwnerId(Long ownerId) {
        return crudRepository.query("""
                        from Car c
                        join fetch c.engine
                        join fetch c.owner
                        left join fetch c.historyOwners
                        where c.owner.id = :ownerId
                        order by c.name asc""", Car.class,
                Map.of("ownerId", ownerId)
        );
    }

    public Optional<Car> findById(Long carId) {
        return crudRepository.optional("""
                        from Car c
                        join fetch c.engine
                        join fetch c.owner
                        left join fetch c.historyOwners
                        where c.id = :fId""", Car.class,
                Map.of("fId", carId)
        );
    }

    public boolean update(Car car) {
        var result = crudRepository.run("""
                        UPDATE Car
                        SET name = :name, engine = :engine, owner = :owner,
                            type = :type, color = :color
                        WHERE id = :id""",
                Map.of("id", car.getId(),
                        "name", car.getName(),
                        "engine", car.getEngine(),
                        "type", car.getType(),
                        "color", car.getColor(),
                        "owner", car.getOwner())
        );
        return result > 0;
    }

    public boolean delete(Long carId, Long ownerId) {
        try {
            var result = crudRepository.run("""
                            delete from Car c
                            where c.id = :fId and c.owner.id = :ownerId""",
                    Map.of("fId", carId,
                            "ownerId", ownerId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error("Delete Car was unsuccessful", e);
        }
        return false;
    }

}
