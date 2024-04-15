package ru.job4j.cars.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.model.Engine;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HbnEngineRepositoryTest {

    @Autowired
    private HbnEngineRepository engineRepository;

    @AfterEach
    public void clearEngines() {
        var engines = engineRepository.findAllOrderById();
        for (var engine : engines) {
            engineRepository.delete(engine.getId());
        }
    }

    @Test
    public void whenAddNewEngineThenFindSameEngine() {
        Engine engine = new Engine();
        engine.setName("test1");
        engineRepository.create(engine);

        Engine result = engineRepository.findById(engine.getId()).get();
        assertThat(result.getName()).isEqualTo(engine.getName());
    }

    @Test
    public void whenUpdateEngineThenReturnTrue() {
        Engine engine = new Engine();

        engine.setName("test1");
        engineRepository.create(engine);
        engine.setName("test2");
        boolean result = engineRepository.update(engine);
        assertThat(result).isTrue();

        Engine updated = engineRepository.findById(engine.getId()).get();
        assertThat(updated.getName()).isEqualTo(engine.getName());
    }

    @Test
    public void whenUpdateUnknownEngineThenReturnFalse() {
        var engine = new Engine();
        boolean result = engineRepository.update(engine);
        assertThat(result).isFalse();
    }

    @Test
    public void whenDeleteEngineThenReturnTrue() {
        Engine engine = new Engine();
        engine.setName("test1");
        engineRepository.create(engine);

        boolean result = engineRepository.delete(engine.getId());
        assertThat(result).isTrue();

        var optional = engineRepository.findById(engine.getId());
        assertThat(optional.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUnknownEngineThenReturnFalse() {
        boolean result = engineRepository.delete(1);
        assertThat(result).isFalse();
    }

    @Test
    public void whenAddEnginesThenGetAllEngines() {
        Engine engine = new Engine();
        engine.setName("test1");
        engineRepository.create(engine);

        Engine item2 = new Engine();
        item2.setName("test2");
        engineRepository.create(item2);

        var expected = Arrays.asList(engine, item2);
        List<Engine> actual = engineRepository.findAllOrderById();
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
