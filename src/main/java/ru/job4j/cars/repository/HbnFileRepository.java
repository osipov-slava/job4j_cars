package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HbnFileRepository implements FileRepository {

    private final CrudRepository crudRepository;

    public File create(File file) {
        crudRepository.run(session -> session.persist(file));
        return file;
    }

    public List<File> findAllOrderById() {
        return crudRepository.query("""
                from File f
                order by f.id asc""", File.class);
    }

    public Optional<File> findById(int fileId) {
        return crudRepository.optional("""
                        from File f
                        where f.id = :fId""", File.class,
                Map.of("fId", fileId)
        );
    }

    @Override
    public List<File> findFilesByPostId(int postId) {
        return crudRepository.query("""
                        from File
                        where post.id = :postId""",
                File.class,
                Map.of("postId", postId));
    }

    public boolean update(File file) {
        try {
            var result = crudRepository.run("UPDATE File SET name = :name, path = :path WHERE id = :id",
                    Map.of("id", file.getId(),
                            "name", file.getName(),
                            "path", file.getPath()));
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean delete(int fileId) {
        try {
            var result = crudRepository.run(
                    "delete from File where id = :fId",
                    Map.of("fId", fileId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteByPostId(int postId) {
        try {
            var result = crudRepository.run(
                    "delete from File where post.id = :postId",
                    Map.of("postId", postId)
            );
            return result > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

}
