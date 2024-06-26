package ru.job4j.cars.repository;

import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    File create(File file);

    List<File> findAllOrderById();

    Optional<File> findById(Long fileId);

    List<File> findFilesByPostId(Long postId);

    boolean update(File file);

    boolean delete(Long fileId);

    boolean deleteByPostId(Long postId);

}
