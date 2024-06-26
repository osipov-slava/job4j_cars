package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.CarMapper;
import ru.job4j.cars.mapstruct.PostMapper;
import ru.job4j.cars.mapstruct.UserMapper;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SimplePostServiceTest {

    private PostService postService;

    private PostRepository postRepository;

    private CarService carService;

    private PriceHistoryService priceHistoryService;

    private FileService fileService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CarMapper carMapper;

    @BeforeEach
    public void initComponents() {
        postRepository = mock(PostRepository.class);
        carService = mock(CarService.class);
        priceHistoryService = mock(PriceHistoryService.class);
        fileService = mock(FileService.class);
        postService = new SimplePostService(postRepository, carService,
                priceHistoryService, fileService, postMapper, userMapper);
    }

    private UserDto initUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        return userDto;
    }

    private CarDto initCarDto(UserDto userDto) {
        CarDto carDto = new CarDto();
        carDto.setId(1L);
        carDto.setName("Toyota Corolla");
        carDto.setOwnerId(1L);
        carDto.setOwnerName(userDto.getName());
        return carDto;
    }

    private List<CarDto> initCarDtos(UserDto userDto) {
        CarDto carDto = initCarDto(userDto);
        CarDto carDto2 = initCarDto(userDto);
        carDto2.setId(2L);
        return List.of(carDto, carDto2);
    }

    private PostDto initPostDto(CarDto carDto, UserDto userDto) {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setDescription("new post");
        postDto.setIsActive(true);
        postDto.setCarId(carDto.getId());
        postDto.setCarName(carDto.getName());
        postDto.setUserId(userDto.getId());
        postDto.setOwnerName(carDto.getOwnerName());
        postDto.setPrice(20000);
        postDto.setPriceHistoryId(1L);
        return postDto;
    }

    private Post initPost(PostDto postDto) {
        return postMapper.getEntityFromDto(postDto);
    }

    private List<PostDto> initPostDtos(List<CarDto> carDtos, UserDto userDto) {
        List<PostDto> postDtos = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PostDto postDto = initPostDto(carDtos.get(0), userDto);
            postDto.setId(1L + i);
            postDto.setPriceHistoryId(1L + i);
            postDtos.add(postDto);
        }
        postDtos.get(1).setIsActive(false);
        postDtos.get(2).setCarId(carDtos.get(1).getId());
        return postDtos;
    }

    private List<Post> initPosts(List<PostDto> postDtos) {
        List<Post> posts = new ArrayList<>();
        for (PostDto postDto : postDtos) {
            posts.add(postMapper.getEntityFromDto(postDto));

        }
        posts.get(2).setFiles(new ArrayList<>());
        File file1 = new File(1L, "/path", "file1", posts.get(2));
        File file2 = new File(2L, "/path", "file2", posts.get(2));
        posts.get(2).getFiles().add(file1);
        posts.get(2).getFiles().add(file2);
        return posts;
    }

    private PriceHistory initPriceHistory(PostDto postDto) {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setId(postDto.getPriceHistoryId());
        priceHistory.setAfter(postDto.getPrice());
        Post post = new Post();
        post.setId(postDto.getId());
        priceHistory.setPost(post);
        return priceHistory;
    }

    private List<PriceHistory> initPriceHistories(List<PostDto> postDtos) {
        List<PriceHistory> priceHistories = new ArrayList<>();
        for (PostDto postDto : postDtos) {
            var ph = initPriceHistory(postDto);
            ph.setId(postDto.getId());
            priceHistories.add(ph);
        }
        return priceHistories;
    }

    @Test
    public void whenCreate() {
        UserDto userDto = initUserDto();
        CarDto carDto = initCarDto(userDto);
        PostDto postDto = initPostDto(carDto, userDto);

        Post post = initPost(postDto);
        List<File> files = new ArrayList<>();
        post.setFiles(files);

        PriceHistory priceHistory = initPriceHistory(postDto);

        when(postRepository.create(post)).thenReturn(post);
        when(priceHistoryService.create(post, postDto.getPrice())).thenReturn(priceHistory);

        var actual = postService.create(postDto, carDto, files);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(postDto);
    }

    @Test
    public void whenFindByIdThenSuccessful() {
        UserDto userDto = initUserDto();
        CarDto carDto = initCarDto(userDto);
        PostDto postDto = initPostDto(carDto, userDto);
        Post post = initPost(postDto);
        PriceHistory priceHistory = initPriceHistory(postDto);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(carService.findById(post.getCar().getId())).thenReturn(Optional.of(carDto));
        when(priceHistoryService.findLastByPostId(post.getId())).thenReturn(Optional.of(priceHistory));

        var actual = postService.findById(post.getId(), userDto);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveAssertion().isEqualTo(postDto);
    }

    @Test
    public void whenFindByIdThenUnsuccessful() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        var actual = carService.findById(1L);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    public void whenFindAll() {
        UserDto userDto = initUserDto();
        List<CarDto> carDtos = initCarDtos(userDto);
        var postDtos = initPostDtos(carDtos, userDto);
        var posts = initPosts(postDtos);
        var priceHistories = initPriceHistories(postDtos);

        when(postRepository.findAllOrderById()).thenReturn(posts);
        when(carService.findAll()).thenReturn(carDtos);
        when(priceHistoryService.findAllLastPrice()).thenReturn(priceHistories);

        var actual = postService.findAll(userDto);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(postDtos);
    }

    @Test
    public void whenFindActive() {
        UserDto userDto = initUserDto();
        List<CarDto> carDtos = initCarDtos(userDto);
        var postDtos = initPostDtos(carDtos, userDto);
        var posts = initPosts(postDtos);
        var priceHistories = initPriceHistories(postDtos);

        when(postRepository.findActive()).thenReturn(posts);
        when(carService.findAll()).thenReturn(carDtos);
        when(priceHistoryService.findAllLastPrice()).thenReturn(priceHistories);

        var actual = postService.findActive(userDto);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(postDtos);
    }

    @Test
    public void whenFindInactive() {
        UserDto userDto = initUserDto();
        List<CarDto> carDtos = initCarDtos(userDto);
        var postDtos = initPostDtos(carDtos, userDto);
        var posts = initPosts(postDtos);
        var priceHistories = initPriceHistories(postDtos);

        when(postRepository.findInactive()).thenReturn(posts);
        when(carService.findAll()).thenReturn(carDtos);
        when(priceHistoryService.findAllLastPrice()).thenReturn(priceHistories);

        var actual = postService.findInactive(userDto);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(postDtos);
    }

    @Test
    public void whenFindWithPhotos() {
        UserDto userDto = initUserDto();
        List<CarDto> carDtos = initCarDtos(userDto);
        var postDtos = initPostDtos(carDtos, userDto);
        var posts = initPosts(postDtos);
        var priceHistories = initPriceHistories(postDtos);

        when(postRepository.findAllWithFile()).thenReturn(posts);
        when(carService.findAll()).thenReturn(carDtos);
        when(priceHistoryService.findAllLastPrice()).thenReturn(priceHistories);

        var actual = postService.findWithPhotos(userDto);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(postDtos);
    }

    @Test
    public void whenUpdateThenSuccessful() {
        UserDto userDto = initUserDto();
        CarDto carDto = initCarDto(userDto);
        PostDto postDto = initPostDto(carDto, userDto);

        when(postRepository.update(postMapper.getEntityFromDto(postDto), userMapper.getEntityFromDto(userDto)))
                .thenReturn(true);

        var actual = postService.update(postDto, userDto);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenUpdateThenUnsuccessful() {
        UserDto userDto = initUserDto();
        CarDto carDto = initCarDto(userDto);
        PostDto postDto = initPostDto(carDto, userDto);

        when(postRepository.update(any(), any())).thenReturn(false);

        var actual = postService.update(postDto, userDto);
        assertThat(actual).isFalse();
    }

    @Test
    public void whenDeleteThenSuccessful() {
        UserDto userDto = initUserDto();

        when(postRepository.delete(1L, userMapper.getEntityFromDto(userDto)))
                .thenReturn(true);
        when(priceHistoryService.deleteAllByPostId(1L)).thenReturn(true);

        var actual = postService.deleteById(1L, userDto);
        assertThat(actual).isTrue();
    }

    @Test
    public void whenDeleteThenUnsuccessful() {
        UserDto userDto = initUserDto();

        when(postRepository.delete(1L, userMapper.getEntityFromDto(userDto)))
                .thenReturn(false);
        when(priceHistoryService.deleteAllByPostId(1L)).thenReturn(true);

        var actual = postService.deleteById(1L, userDto);
        assertThat(actual).isFalse();
    }

}
