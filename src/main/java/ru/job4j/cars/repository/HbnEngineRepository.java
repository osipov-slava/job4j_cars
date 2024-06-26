package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnEngineRepository implements EngineRepository {

    private final CrudRepository crudRepository;

    public Engine create(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public List<Engine> findAllOrderById() {
        return crudRepository.query("from Engine order by id asc", Engine.class);
    }

    public Optional<Engine> findById(Long engineId) {
        return crudRepository.optional("from Engine where id = :fId", Engine.class,
                Map.of("fId", engineId));
    }

    public boolean update(Engine engine) {
        try {
            var result = crudRepository.run("UPDATE Engine SET name = :name WHERE id = :id",
                    Map.of("id", engine.getId(),
                            "name", engine.getName()));
            return result > 0;
        } catch (Exception e) {
            log.error("Update Engine was unsuccessful", e);
        }
        return false;
    }

    public boolean delete(Long engineId) {
        try {
            var result = crudRepository.run("delete from Engine where id = :fId",
                    Map.of("fId", engineId));
            return result > 0;
        } catch (Exception e) {
            log.error("Delete Engine was unsuccessful", e);
        }
        return false;
    }

}
