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
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.PostRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    private final CarRepository carRepository;

    private final CarService carService;

    private final PostMapper postMapper;

    private final UserMapper userMapper;

    @Override
    public PostDto create(PostDto postDto, UserDto userDto, CarDto carDto) {
        Post post = postMapper.getEntityFromDto(postDto);
        post = postRepository.create(post);
        PostDto postDto1 = postMapper.getModelFromEntity(post, carDto, 10000);
        return postDto1;
    }

    @Override
    public Optional<PostDto> findById(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        Optional<PostDto> optionalPostDto = Optional.empty();
        if (optionalPost.isEmpty()) {
            return optionalPostDto;
        }
        var post = optionalPost.get();
        Optional<CarDto> optionalCarDto = carService.findById(post.getCar().getId());
        optionalPostDto = Optional.of(postMapper.getModelFromEntity(post, optionalCarDto.get(), 10000));
        return optionalPostDto;
    }

    @Override
    public List<PostDto> findAll() {
        List<Post> posts = postRepository.findAllOrderById();
        List<CarDto> carDtos= carService.findAll();
        Map<Integer, CarDto> mapAllCarDtos = carDtos.stream().
                collect(Collectors.toMap(CarDto::getId, carDto -> carDto));
        List<PostDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(postMapper.getModelFromEntity(post, mapAllCarDtos.get(post.getCar().getId()), 10000));
        }
        return postDtos;
    }

    @Override
    public boolean update(PostDto postDto, UserDto userDto) {
        return postRepository.update(postMapper.getEntityFromDto(postDto), userMapper.getEntityFromDto(userDto));
    }

//    @Override
//    public boolean done(int id, UserDto userDto) {
//        return postRepository.done(id, userDto);
//    }
//
    @Override
    public boolean deleteById(int id, UserDto userDto) {
        return postRepository.delete(id, userMapper.getEntityFromDto(userDto));
    }
}
