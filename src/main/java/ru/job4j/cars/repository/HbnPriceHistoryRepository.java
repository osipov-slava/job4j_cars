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

    public List<PriceHistory> findAllByPostId(int postId) {
        return crudRepository.query("""
                        FROM PriceHistory ph
                        WHERE post.id = :postId
                        ORDER BY created DESC""", PriceHistory.class,
                Map.of("postId", postId)
        );
    }

    public List<PriceHistory> findAllLastPrice() {
        return crudRepository.query("""
                FROM PriceHistory
                WHERE (post.id, created) IN (
                    SELECT post.id, MAX(created)
                    FROM PriceHistory
                    GROUP BY post.id
                )""", PriceHistory.class
        );
    }

    public Optional<PriceHistory> findById(int priceHistoryId) {
        return crudRepository.optional("""
                        FROM PriceHistory ph
                        WHERE ph.id = :id""", PriceHistory.class,
                Map.of("id", priceHistoryId)
        );
    }

    public Optional<PriceHistory> findLastByPostId(int postId) {
        return crudRepository.optional("""
                        FROM PriceHistory ph
                        WHERE post.id = :postId
                        AND created = (
                            SELECT MAX(created)
                            FROM ph
                            WHERE post.id = :postId
                        )""", PriceHistory.class,
                Map.of("postId", postId)
        );
    }

    public boolean deleteAllByPostId(int postId) {
        try {
            var result = crudRepository.run("""
                            DELETE FROM PriceHistory
                            WHERE post.id = :postId""",
                    Map.of("postId", postId)
            );
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

}
