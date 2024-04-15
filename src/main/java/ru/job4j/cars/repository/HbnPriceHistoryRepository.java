package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnPriceHistoryRepository implements PriceHistoryRepository {

    private final CrudRepository crudRepository;

    public PriceHistory create(PriceHistory priceHistory) {
        crudRepository.run(session -> session.persist(priceHistory));
        return priceHistory;
    }

    public List<PriceHistory> findAllOrderById() {
        return crudRepository.query("""
                from PriceHistory ph
                join fetch ph.post
                order by ph.id asc""", PriceHistory.class);
    }

    public Optional<PriceHistory> findById(int priceHistoryId) {
        return crudRepository.optional("""
                from PriceHistory ph
                join fetch ph.post
                where ph.id = :fId""", PriceHistory.class,
                Map.of("fId", priceHistoryId)
        );
    }

    public boolean update(PriceHistory priceHistory) {
        try {
            var result = crudRepository.run(
                    "UPDATE PriceHistory SET before = :before, after = :after WHERE id = :id",
                    Map.of("id", priceHistory.getId(),
                            "before", priceHistory.getBefore(),
                            "after", priceHistory.getAfter()));
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean delete(int priceHistoryId) {
        try {
            var result = crudRepository.run(
                    "delete from PriceHistory where id = :fId",
                    Map.of("fId", priceHistoryId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
