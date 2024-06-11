package ru.job4j.cars.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class IndexControllerTest {

    @Test
    public void whenRequestIndexThenRedirectIndexPage() {
        var indexController = new IndexController();
        var view = indexController.getIndex();

        assertThat(view).isEqualTo("index");
    }
}
