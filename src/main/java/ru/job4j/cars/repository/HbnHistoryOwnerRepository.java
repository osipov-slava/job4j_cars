package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.HistoryOwner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnHistoryOwnerRepository implements HistoryOwnerRepository {

    private final CrudRepository crudRepository;

    public HistoryOwner create(HistoryOwner historyOwner) {
        crudRepository.run(session -> session.persist(historyOwner));
        return historyOwner;
    }

    public List<HistoryOwner> findAllOrderById() {
        return crudRepository.query("""
                from HistoryOwner ho
                join fetch ho.owner
                join fetch ho.car
                order by ho.id asc""", HistoryOwner.class);
    }

    public Optional<HistoryOwner> findById(int historyOwnerId) {
        return crudRepository.optional("""
                        from HistoryOwner ho
                        join fetch ho.owner
                        join fetch ho.car
                        where ho.id = :fId""", HistoryOwner.class,
                Map.of("fId", historyOwnerId)
        );
    }

    public boolean update(HistoryOwner historyOwner) {
        try {
            var result = crudRepository.run("""
                            UPDATE HistoryOwner
                            SET startAt = :startAt, endAt = :endAt, car = :car, owner = :owner
                            WHERE id = :id""",
                    Map.of("id", historyOwner.getId(),
                            "startAt", historyOwner.getStartAt(),
                            "endAt", historyOwner.getEndAt(),
                            "car", historyOwner.getCar(),
                            "owner", historyOwner.getOwner()));
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean delete(int historyOwnerId) {
        try {
            var result = crudRepository.run(
                    "delete from HistoryOwner where id = :fId",
                    Map.of("fId", historyOwnerId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

}
