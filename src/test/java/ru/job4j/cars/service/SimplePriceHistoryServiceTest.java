package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.PriceHistoryDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.PriceHistoryMapper;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PriceHistoryRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SimplePriceHistoryServiceTest {

    private PriceHistoryService priceHistoryService;

    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private PriceHistoryMapper priceHistoryMapper;

    @BeforeEach
    public void initComponents() {
        priceHistoryRepository = mock(PriceHistoryRepository.class);
        priceHistoryService = new SimplePriceHistoryService(priceHistoryRepository, priceHistoryMapper);
    }

    private Post initPost() {
        Post post = new Post();
        post.setId(1L);
        return post;
    }

    private PriceHistory initPriceHistory(Post post) {
        return new PriceHistory(1L, 10000, 9000, null, post);
    }

    private List<PriceHistory> initListPriceHistory(PriceHistory firstPriceHistory) {
        firstPriceHistory.setCreated(LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS));
        var secondPriceHistory = new PriceHistory(2L, 15000, 14000,
                LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS),
                firstPriceHistory.getPost());
        return List.of(firstPriceHistory, secondPriceHistory);
    }

    private List<PriceHistoryDto> initPriceHistoryDtos(List<PriceHistory> priceHistories) {
        List<PriceHistoryDto> priceHistoryDtos = new ArrayList<>();
        for (PriceHistory ph : priceHistories) {
            priceHistoryDtos.add(priceHistoryMapper.getModelFromEntity(ph));
        }
        return priceHistoryDtos;
    }

    @Test
    public void whenCreatePriceHistory() {
        Post post = initPost();
        PriceHistory priceHistory = new PriceHistory(null, 0, 9000, null, post);
        when(priceHistoryRepository.create(priceHistory)).thenReturn(priceHistory);

        var actual = priceHistoryService.create(post, 9000);
        assertThat(actual).isEqualTo(priceHistory);
    }

    @Test
    public void whenFindByIdThenSuccessful() {
        PriceHistory priceHistory = initPriceHistory(initPost());
        when(priceHistoryRepository.findById(1L)).thenReturn(Optional.of(priceHistory));

        var actual = priceHistoryService.findById(1L);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveAssertion().isEqualTo(priceHistory);
    }

    @Test
    public void whenFindByIdThenUnsuccessful() {
        when(priceHistoryRepository.findById(1L)).thenReturn(Optional.empty());
        var actual = priceHistoryService.findById(1L);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    public void whenFindAllByPostId() {
        var priceHistories = initListPriceHistory(initPriceHistory(initPost()));
        when(priceHistoryRepository.findAllByPostId(1L)).thenReturn(priceHistories);

        var expected = initPriceHistoryDtos(priceHistories);
        var actual = priceHistoryService.findAllByPostId(1L, new UserDto());
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("created").isEqualTo(expected);
    }

    @Test
    public void whenFindAllLastPrice() {
        var priceHistories = initListPriceHistory(initPriceHistory(initPost()));
        when(priceHistoryRepository.findAllLastPrice()).thenReturn(priceHistories);

        var actual = priceHistoryService.findAllLastPrice();
        assertThat(actual).isEqualTo(priceHistories);
    }

    @Test
    public void whenUpdateThenSuccessful() {
        PostDto postDto = new PostDto();
        postDto.setPriceHistoryId(1L);
        PriceHistory priceHistory = initPriceHistory(initPost());
        when(priceHistoryRepository.findById(1L)).thenReturn(Optional.of(priceHistory));

        var actual = priceHistoryService.update(postDto, 11111);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenUpdateThenUnsuccessful() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        when(priceHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        var actual = priceHistoryService.update(postDto, 11111);
        assertThat(actual).isFalse();
    }

    @Test
    public void whenDeleteAllPostsById() {
        when(priceHistoryRepository.deleteAllByPostId(1L)).thenReturn(true);
        var actual = priceHistoryService.deleteAllByPostId(1L);
        assertThat(actual).isTrue();

        when(priceHistoryRepository.deleteAllByPostId(2L)).thenReturn(false);
        actual = priceHistoryService.deleteAllByPostId(2L);
        assertThat(actual).isFalse();
    }

}
