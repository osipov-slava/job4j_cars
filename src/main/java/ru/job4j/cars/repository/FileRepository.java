package ru.job4j.cars.repository;

import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    File create(File file);

    List<File> findAllOrderById();

    Optional<File> findById(int fileId);

    List<File> findFilesByPostId(int postId);

    boolean update(File file);

    boolean delete(int fileId);

    boolean deleteByPostId(int postId);
}
