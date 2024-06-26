package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.File;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HbnFileRepositoryTest {

    @Autowired
    private HbnFileRepository fileRepository;

    @AfterEach
    public void clearFiles() {
        var files = fileRepository.findAllOrderById();
        for (var file : files) {
            fileRepository.delete(file.getId());
        }
    }

    private File initFile() {
        File file = new File();
        file.setName("file1");
        file.setPath("path1");
        fileRepository.create(file);
        return file;
    }

    @Test
    public void whenAddNewFileThenFindSameFile() {
        File file = initFile();

        File result = fileRepository.findById(file.getId()).get();
        assertThat(result).usingRecursiveAssertion().isEqualTo(file);
    }

    @Test
    public void whenUpdateFileThenReturnTrue() {
        File file = initFile();

        file.setName("file2");
        file.setPath("path2");
        boolean result = fileRepository.update(file);
        assertThat(result).isTrue();

        File updated = fileRepository.findById(file.getId()).get();
        assertThat(updated).usingRecursiveAssertion().isEqualTo(file);
    }

    @Test
    public void whenUpdateUnknownFileThenReturnFalse() {
        var file = new File();
        boolean result = fileRepository.update(file);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeleteFileThenReturnTrue() {
        File file = initFile();

        boolean result = fileRepository.delete(file.getId());
        assertThat(result).isTrue();

        var optional = fileRepository.findById(file.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownFileThenReturnFalse() {
        boolean result = fileRepository.delete(1L);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddFilesThenGetAllFiles() {
        File file = initFile();

        File file2 = new File();
        file2.setName("file1");
        file2.setPath("path1");
        fileRepository.create(file2);

        var expected = Arrays.asList(file, file2);
        List<File> actual = fileRepository.findAllOrderById();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
