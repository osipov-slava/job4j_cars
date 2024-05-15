package ru.job4j.cars.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Optional;

public interface FileService {

    File save(FileDto fileDto);

    Optional<FileDto> getFileById(int id);

    List<Integer> getFileIdsByPostId(int postId);

//    void deleteById(int id);

    void deleteByPostId(int postId);

    List<File> createFilesFromMultipartFiles(MultipartFile[] multipartFiles);

    List<File> updateFilesFromMultipartFiles(MultipartFile[] multipartFiles, PostDto postDto);

}