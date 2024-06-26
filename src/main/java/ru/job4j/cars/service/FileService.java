package ru.job4j.cars.service;

import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.File;

import java.util.List;
import java.util.Optional;

public interface FileService {

    File save(FileDto fileDto);

    Optional<FileDto> getFileById(Long id);

    List<Long> getFileIdsByPostId(Long postId);

    void deleteByPostId(Long postId);

    List<File> createFilesFromMultipartFiles(MultipartFile[] multipartFiles);

    void updateFilesFromMultipartFiles(MultipartFile[] multipartFiles, PostDto postDto);

}
