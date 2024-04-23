package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    /**
     * Список пользователь отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        return crudRepository.query("FROM User ORDER BY id ASC", User.class);
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "FROM User WHERE id = :id", User.class,
                Map.of("id", userId)
        );
    }

    /**
     * Find user by email and password.
     *
     * @param email    email.
     * @param password password.
     * @return Optional of user.
     */
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional("""
                FROM User u
                WHERE u.email = :email
                AND u.password = :password
                """, User.class,
                Map.of("email", email,
                        "password", password)
        );
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User WHERE login LIKE :key", User.class,
                Map.of("key", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional of user.
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "FROM User WHERE login = :login", User.class,
                Map.of("login", login)
        );
    }

    /**
     * Обновить в базе пользователя.
     *
     * @param user пользователь.
     */
    public boolean update(User user) {
        try {
            var result = crudRepository.run("""
                            UPDATE User
                            SET login = :login, password = :password, email = :email
                            WHERE id = :id""",
                    Map.of("id", user.getId(),
                            "login", user.getLogin(),
                            "password", user.getPassword(),
                            "email", user.getEmail()));
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    public boolean delete(int userId) {
        try {
            var result = crudRepository.run(
                    "DELETE FROM User WHERE id = :id",
                    Map.of("id", userId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
