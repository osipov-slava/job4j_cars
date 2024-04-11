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

    public List<Car> findAllOrderById() {
        return crudRepository.query("""
                from Car c
                join fetch c.engine
                join fetch c.owner
                left join fetch c.historyOwners
                order by c.id asc""", Car.class);
    }

    public Optional<Car> findById(int carId) {
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
        try {
            crudRepository.run(session -> session.merge(car));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean delete(int carId) {
        try {
            var result = crudRepository.run(
                    "delete from Car where id = :fId",
                    Map.of("fId", carId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

}
