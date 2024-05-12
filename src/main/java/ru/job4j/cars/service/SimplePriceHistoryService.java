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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePriceHistoryService implements PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    private final PriceHistoryMapper priceMapper;

    @Override
    public PriceHistory create(Post post, long price) {
        var priceHistory = new PriceHistory();
        priceHistory.setPost(post);
        priceHistory.setAfter(price);
        return priceHistoryRepository.create(priceHistory);
    }

    @Override
    public Optional<PriceHistory> findById(int id) {
        return priceHistoryRepository.findById(id);
    }

    @Override
    public Optional<PriceHistory> findLastByPostId(int postId) {
        return priceHistoryRepository.findLastByPostId(postId);
    }

//    @Override
//    public List<PriceHistory> findAll() {
//        return priceHistoryRepository.findAllOrderById();
//    }

    @Override
    public List<PriceHistoryDto> findAllByPostId(int postId, UserDto userDto) {
        var priceHistories = priceHistoryRepository.findAllByPostId(postId);
        List<PriceHistoryDto> priceDtos = new ArrayList<>();
        for (PriceHistory ph : priceHistories) {
            ph.setCreated(Utils.correctTimeZone(ph.getCreated(), userDto.getTimezone()));
            priceDtos.add(priceMapper.getModelFromEntity(ph));
        }
        return priceDtos;
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
            priceHistory.setBefore(optionalPH.get().getAfter());
            priceHistory.setPost(post);
            priceHistory.setAfter(price);
            priceHistoryRepository.create(priceHistory);
            return true;
        }
        return false;
    }

//    @Override
//    public boolean deleteById(int id) {
//        return priceHistoryRepository.delete(id);
//    }

    @Override
    public boolean deleteAllByPostId(int postId) {
        return priceHistoryRepository.deleteAllByPostId(postId);
    }

}
