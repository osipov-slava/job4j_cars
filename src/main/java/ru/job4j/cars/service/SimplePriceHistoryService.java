package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.PriceHistoryDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.PriceHistoryMapper;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PriceHistoryRepository;
import ru.job4j.cars.util.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePriceHistoryService implements PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    private final PriceHistoryMapper priceHistoryMapper;

    @Override
    public PriceHistory create(Post post, long price) {
        var priceHistory = new PriceHistory();
        priceHistory.setCreated(LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS));
        priceHistory.setPost(post);
        priceHistory.setAfter(price);
        return priceHistoryRepository.create(priceHistory);
    }

    @Override
    public Optional<PriceHistory> findById(Long id) {
        return priceHistoryRepository.findById(id);
    }

    @Override
    public Optional<PriceHistory> findLastByPostId(Long postId) {
        return priceHistoryRepository.findLastByPostId(postId);
    }

    @Override
    public List<PriceHistoryDto> findAllByPostId(Long postId, UserDto userDto) {
        var priceHistories = priceHistoryRepository.findAllByPostId(postId);
        List<PriceHistoryDto> priceHistoryDtos = new ArrayList<>();
        for (PriceHistory ph : priceHistories) {
            ph.setCreated(Utils.correctTimeZone(ph.getCreated(), userDto.getTimezone()));
            priceHistoryDtos.add(priceHistoryMapper.getModelFromEntity(ph));
        }
        return priceHistoryDtos;
    }

    @Override
    public List<PriceHistory> findAllLastPrice() {
        return priceHistoryRepository.findAllLastPrice();
    }

    @Override
    public boolean update(PostDto postDto, long price) {
        var priceHistory = new PriceHistory();
        var post = new Post();
        post.setId(postDto.getId());
        var optionalPH = priceHistoryRepository.findById(postDto.getPriceHistoryId());
        if (optionalPH.isPresent() && optionalPH.get().getAfter() != price) {
            priceHistory.setCreated(LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS));
            priceHistory.setBefore(optionalPH.get().getAfter());
            priceHistory.setPost(post);
            priceHistory.setAfter(price);
            priceHistoryRepository.create(priceHistory);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAllByPostId(Long postId) {
        return priceHistoryRepository.deleteAllByPostId(postId);
    }

}
