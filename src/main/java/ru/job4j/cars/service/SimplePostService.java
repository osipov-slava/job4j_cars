package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.PostMapper;
import ru.job4j.cars.mapstruct.UserMapper;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.util.Utils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    private final CarService carService;

    private final PriceHistoryService priceHistoryService;

    private final FileService fileService;

    private final PostMapper postMapper;

    private final UserMapper userMapper;

    @Override
    public PostDto create(PostDto postDto, UserDto userDto, CarDto carDto, List<File> files) {
        Post post = postMapper.getEntityFromDto(postDto);
        post.setFiles(files);
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
        var post = Utils.correctTimeZone(optionalPost.get(), userDto.getTimezone());

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
        return makePostDtosWithAdditionalData(posts, userDto);
    }

    private List<PostDto> makePostDtosWithAdditionalData (List<Post> posts, UserDto userDto) {
        List<CarDto> carDtos = carService.findAll();
        Map<Integer, CarDto> mapAllCarDtos = carDtos.stream().
                collect(Collectors.toMap(CarDto::getId, carDto -> carDto));

        List<PriceHistory> priceHistories = priceHistoryService.findAllLastPrice();
        Map<Integer, PriceHistory> mapAllPH = new HashMap<>();
        for (PriceHistory ph : priceHistories) {
            mapAllPH.put(ph.getPost().getId(), ph);
        }

        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            Utils.correctTimeZone(post, userDto.getTimezone());
            postDtos.add(postMapper.getModelFromEntity(post, mapAllCarDtos.get(post.getCar().getId()), mapAllPH.get(post.getId())));
        }
        return postDtos;
    }

    @Override
    public List<PostDto> findActive(UserDto userDto) {
        List<Post> posts = postRepository.findActive();
        return makePostDtosWithAdditionalData(posts, userDto);
    }

    @Override
    public List<PostDto> findInactive(UserDto userDto) {
        List<Post> posts = postRepository.findInactive();
        return makePostDtosWithAdditionalData(posts, userDto);
    }

    @Override
    public List<PostDto> findWithPhotos(UserDto userDto) {
        List<Post> posts = postRepository.findAllWithFile();
        return makePostDtosWithAdditionalData(posts, userDto);
    }

    @Override
    public List<PostDto> findLastDay(UserDto userDto) {
        List<Post> posts = postRepository.findAllForLastDay();
        return makePostDtosWithAdditionalData(posts, userDto);
    }

    @Override
    public boolean update(PostDto postDto, UserDto userDto) {
        Post post = postMapper.getEntityFromDto(postDto);
        priceHistoryService.update(postDto, postDto.getPrice());
        return postRepository.update(post, userMapper.getEntityFromDto(userDto));
    }

    @Override
    public boolean deleteById(int id, UserDto userDto) {
        if (!priceHistoryService.deleteAllByPostId(id)) {
            return false;
        }
        fileService.deleteByPostId(id);
        return postRepository.delete(id, userMapper.getEntityFromDto(userDto));
    }

}
