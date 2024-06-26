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
     * Create new user in database.
     *
     * @param user user.
     * @return user with id.
     */
    @Override
    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    /**
     * List of users ordered by id.
     *
     * @return user's list.
     */
    @Override
    public List<User> findAllOrderById() {
        return crudRepository.query("FROM User ORDER BY id ASC", User.class);
    }

    /**
     * Find user by ID.
     *
     * @return user.
     */
    @Override
    public Optional<User> findById(Long userId) {
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
     * Update user in database.
     *
     * @param user user.
     */
    @Override
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
            log.error("Update User was unsuccessful", e);
        }
        return false;
    }

    /**
     * Delete user from database by id.
     *
     * @param userId ID.
     * @return is updated.
     */
    @Override
    public boolean delete(Long userId) {
        try {
            var result = crudRepository.run(
                    "DELETE FROM User WHERE id = :id",
                    Map.of("id", userId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error("Delete User was unsuccessful", e);
        }
        return false;
    }

}
