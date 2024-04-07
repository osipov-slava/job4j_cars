package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnOwnerRepository implements OwnerRepository {

    private final CrudRepository crudRepository;

    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    public List<Owner> findAllOrderById() {
        return crudRepository.query("""
                from Owner o
                order by o.id asc""", Owner.class);
    }

    public Optional<Owner> findById(int ownerId) {
        return crudRepository.optional("""
                        from Owner o
                        where o.id = :fId""", Owner.class,
                Map.of("fId", ownerId)
        );
    }

    public boolean update(Owner owner) {
        try {
            crudRepository.run(session -> session.merge(owner));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean delete(int ownerId) {
        try {
            var result = crudRepository.run(
                    "delete from Owner where id = :fId",
                    Map.of("fId", ownerId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}