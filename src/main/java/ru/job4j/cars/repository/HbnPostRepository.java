package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnPostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    private final SessionFactory sf;

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public List<Post> findAllOrderById() {
        return crudRepository.query("""
                select distinct p
                from Post p
                left join fetch p.user
                left join fetch p.car
                left join fetch p.files
                order by p.id asc""", Post.class);
    }

    public Optional<Post> findById(int postId) {
        return crudRepository.optional("""
                        select distinct p
                        from Post p
                        left join fetch p.user
                        left join fetch p.car
                        left join fetch p.files
                        where p.id = :fId""", Post.class,
                Map.of("fId", postId));
    }

    public List<Post> findPostsWithSubscribersByPosts(List<Post> posts) {
        Session session = sf.openSession();
        Transaction transaction = null;
        List<Post> postList = Collections.emptyList();
        try {
            transaction = session.beginTransaction();
            postList = session.createQuery("""
                            select distinct p
                            from Post p
                            left join fetch p.user
                            left join fetch p.car
                            left join fetch p.files
                            where p in (:posts)""", Post.class)
                    .setParameter("posts", posts)
                    .getResultList();
            postList = session.createQuery("""
                            select distinct p
                            from Post p
                            left join fetch p.subscribers
                            where p in (:posts)""", Post.class)
                    .setParameter("posts", postList)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return postList;
    }

    public List<Post> findAllForLastDay() {
        var previousDay = LocalDateTime.now().minusDays(1);
        return crudRepository.query("""
                        select distinct p
                        from Post p
                        left join fetch p.user
                        left join fetch p.car
                        left join fetch p.files
                        where p.created > :previous""", Post.class,
                Map.of("previous", previousDay));
    }

    public List<Post> findAllWithFile() {
        return crudRepository.query("""
                select distinct p
                from Post p
                left join fetch p.user
                left join fetch p.car
                left join fetch p.files
                where p.files is not empty""", Post.class);
    }

    public List<Post> findAllCarModelLike(String key) {
        return crudRepository.query("""
                        select distinct p
                        from Post p
                        left join fetch p.user
                        left join fetch p.car
                        left join fetch p.files
                        where p.car.name like :key""", Post.class,
                Map.of("key", "%" + key + "%"));
    }

    public boolean update(Post post, User user) {
        try {
            var result = crudRepository.run("""
                    UPDATE Post
                    SET description = :description
                    WHERE id = :id AND user = :user""",
                    Map.of("id", post.getId(),
                            "description", post.getDescription(),
                            "user", user));
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean delete(int postId, User user) {
        try {
            var result = crudRepository.run("""
                    delete from Post
                    where id = :id and user = :user""",
                    Map.of("id", postId,
                            "user", user)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
