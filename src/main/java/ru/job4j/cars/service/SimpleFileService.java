package ru.job4j.cars.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
public class SimpleFileService implements FileService {

    private final FileRepository fileRepository;

    private final String storageDirectory;

    public SimpleFileService(FileRepository fileRepository,
                             @Value("${file.directory}") String storageDirectory) {
        this.fileRepository = fileRepository;
        this.storageDirectory = storageDirectory;
        createStorageDirectory(storageDirectory);
    }

    private void createStorageDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File save(FileDto fileDto) {
        var path = getNewFilePath(fileDto.getName());
        writeFileBytes(path, fileDto.getContent());

        var file = new File();
        file.setName(fileDto.getName());
        file.setPath(path);
        return file;
    }

    @Override
    public List<File> createFilesFromMultipartFiles(MultipartFile[] multipartFiles) {
        List<File> files = new ArrayList<>();
        if (isEmptyMultipartFiles(multipartFiles)) {
            return files;
        }
        Arrays.stream(multipartFiles).forEach(file -> {
            try {
                files.add(save(new FileDto(file.getOriginalFilename(), file.getBytes())));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return files;
    }

    @Override
    public void updateFilesFromMultipartFiles(MultipartFile[] multipartFiles, PostDto postDto) {
        List<File> files = createFilesFromMultipartFiles(multipartFiles);
        if (files.isEmpty()) {
            return;
        }
        deleteByPostId(postDto.getId());

        var post = new Post();
        post.setId(postDto.getId());
        for (File file : files) {
            file.setPost(post);
            fileRepository.create(file);
        }
    }

    private boolean isEmptyMultipartFiles(MultipartFile[] multipartFiles) {
        try {
            if (multipartFiles[0].getBytes().length == 0) {
                return true;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private String getNewFilePath(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    private void writeFileBytes(String path, byte[] content) {
        try {
            Files.write(Path.of(path), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FileDto> getFileById(Long id) {
        var fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty()) {
            return Optional.empty();
        }
        var content = readFileAsBytes(fileOptional.get().getPath());
        return Optional.of(new FileDto(fileOptional.get().getName(), content));
    }

    @Override
    public List<Long> getFileIdsByPostId(Long postId) {
        var files = fileRepository.findFilesByPostId(postId);
        List<Long> ids = new ArrayList<>();
        for (File file : files) {
            ids.add(file.getId());
        }
        return ids;
    }

    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByPostId(Long postId) {
        var files = fileRepository.findFilesByPostId(postId);
        if (fileRepository.deleteByPostId(postId)) {
            for (File file : files) {
                deleteFile(file.getPath());
            }
        }
    }

    private void deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
