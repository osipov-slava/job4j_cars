package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Type;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnTypeRepository implements TypeRepository {

    private final CrudRepository crudRepository;

    @Override
    public Type create(Type type) {
        crudRepository.run(session -> session.persist(type));
        return type;
    }

    @Override
    public List<Type> findAll() {
        return crudRepository.query("FROM Type", Type.class);
    }

    @Override
    public boolean deleteById(Integer id) {
        try {
            var result = crudRepository.run(
                    "DELETE FROM Type WHERE id = :id",
                    Map.of("id", id)
            );
            return result > 0;
        } catch (Exception e) {
            log.error("Delete Type was unsuccessful", e);
        }
        return false;
    }

}
