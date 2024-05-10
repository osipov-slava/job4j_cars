package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.PostMapper;
import ru.job4j.cars.mapstruct.UserMapper;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.UserRepository;
import ru.job4j.cars.util.Utils;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    private final CarService carService;

    private final PriceHistoryService priceHistoryService;

    private final PostMapper postMapper;

    private final UserMapper userMapper;

    @Override
    public PostDto create(PostDto postDto, UserDto userDto, CarDto carDto) {
        Post post = postMapper.getEntityFromDto(postDto);
        post = postRepository.create(post);
        var priceHistory = priceHistoryService.create(post, postDto.getPrice());
        return postMapper.getModelFromEntity(post, carDto, priceHistory);
    }

    @Override
    public Optional<PostDto> findById(int id, UserDto userDto) {
        //TODO Упростить или каскадом подтягивать
        Optional<PostDto> optionalPostDto = Optional.empty();
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return optionalPostDto;
        }
        var post = optionalPost.get();
        post = Utils.correctTimeZone(post, userDto.getTimezone());

        Optional<CarDto> optionalCarDto = carService.findById(post.getCar().getId());
        var carDto = optionalCarDto.orElseGet(CarDto::new);

        Optional<PriceHistory> optionalPriceHistory = priceHistoryService.findLastByPostId(id);
        var priceHistory = optionalPriceHistory.orElseGet(PriceHistory::new);

        optionalPostDto = Optional.of(postMapper.getModelFromEntity(post, carDto, priceHistory));
        return optionalPostDto;
    }

    @Override
    public List<PostDto> findAll(UserDto userDto) {
        List<Post> posts = postRepository.findAllOrderById();

        List<CarDto> carDtos= carService.findAll();
        Map<Integer, CarDto> mapAllCarDtos = carDtos.stream().
                collect(Collectors.toMap(CarDto::getId, carDto -> carDto));

        List<PriceHistory> priceHistories = priceHistoryService.findAllLastPrice();
        Map<Integer, PriceHistory> mapAllPH = new HashMap<>();
        for (PriceHistory ph : priceHistories) {
            mapAllPH.put(ph.getPost().getId(), ph);
        }

        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            post = Utils.correctTimeZone(post, userDto.getTimezone());
            postDtos.add(postMapper.getModelFromEntity(post, mapAllCarDtos.get(post.getCar().getId()), mapAllPH.get(post.getId())));
        }
        return postDtos;
    }

    @Override
    public boolean update(PostDto postDto, UserDto userDto) {
        priceHistoryService.update(postDto, postDto.getPrice());
        return postRepository.update(postMapper.getEntityFromDto(postDto), userMapper.getEntityFromDto(userDto));
    }

//    @Override
//    public boolean done(int id, UserDto userDto) {
//        return postRepository.done(id, userDto);
//    }
//
    @Override
    public boolean deleteById(int id, UserDto userDto) {
        if (!priceHistoryService.deleteAllByPostId(id)) {
            return false;
        }
        return postRepository.delete(id, userMapper.getEntityFromDto(userDto));
    }
}
