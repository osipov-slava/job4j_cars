package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnPostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public List<Post> findAllOrderById() {
        return crudRepository.query("""
                select distinct p
                from Post p
                join fetch p.priceHistory
                join fetch p.user
                order by id asc""", Post.class);
    }

    public Optional<Post> findById(int postId) {
        return crudRepository.optional("""
                        select distinct p
                        from Post p
                        join fetch p.priceHistory
                        join fetch p.user
                        where id = :fId""", Post.class,
                Map.of("fId", postId)
        );
    }

    public boolean update(Post post) {
        try {
            crudRepository.run(session -> session.merge(post));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean delete(int postId) {
        try {
            var result = crudRepository.run(
                    "delete from Post where id = :fId",
                    Map.of("fId", postId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
