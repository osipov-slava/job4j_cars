package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Color;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnColorRepository implements ColorRepository {

    private final CrudRepository crudRepository;

    @Override
    public Color create(Color color) {
        crudRepository.run(session -> session.persist(color));
        return color;
    }

    @Override
    public List<Color> findAll() {
        return crudRepository.query("FROM Color", Color.class);
    }

    @Override
    public boolean deleteById(Integer id) {
        try {
            var result = crudRepository.run(
                    "DELETE FROM Color WHERE id = :id",
                    Map.of("id", id)
            );
            return result > 0;
        } catch (Exception e) {
            log.error("Delete Color was unsuccessful", e);
        }
        return false;
    }

}
