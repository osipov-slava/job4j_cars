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
                join fetch o.user
                left join fetch o.historyOwners
                order by o.id asc""", Owner.class);
    }

    public Optional<Owner> findById(Long ownerId) {
        return crudRepository.optional("""
                        from Owner o
                        join fetch o.user
                        left join fetch o.historyOwners
                        where o.id = :fId""", Owner.class,
                Map.of("fId", ownerId)
        );
    }

    public Optional<Owner> findByUserId(Long userId) {
        return crudRepository.optional("""
                        from Owner o
                        where o.user.id = :userId""", Owner.class,
                Map.of("userId", userId)
        );
    }

    public boolean update(Owner owner) {
        try {
            var result = crudRepository.run("UPDATE Owner SET name = :name, user = :user WHERE id = :id",
                    Map.of("id", owner.getId(),
                            "name", owner.getName(),
                            "user", owner.getUser()));
            return result > 0;
        } catch (Exception e) {
            log.error("Update Owner was unsuccessful", e);
        }
        return false;
    }

    public boolean delete(Long ownerId) {
        try {
            var result = crudRepository.run(
                    "delete from Owner where id = :fId",
                    Map.of("fId", ownerId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error("Delete Owner was unsuccessful", e);
        }
        return false;
    }

}
