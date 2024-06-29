package ru.job4j.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.PriceHistoryDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.File;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.FileService;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.service.PriceHistoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostControllerTest {

    private PostController postController;

    private PostService postService;

    private FileService fileService;

    private CarService carService;

    private PriceHistoryService priceHistoryService;

    @BeforeEach
    public void initComponents() {
        postService = mock(PostService.class);
        fileService = mock(FileService.class);
        carService = mock(CarService.class);
        priceHistoryService = mock(PriceHistoryService.class);
        postController = new PostController(postService, fileService, carService, priceHistoryService);
    }

    private UserDto initUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setTimezone("UTC");
        userDto.setName("User Name");
        userDto.setPassword("password");
        userDto.setEmail("mail@mail.com");
        userDto.setLogin("user");
        userDto.setOwnerId(2L);
        return userDto;
    }

    private CarDto initCarDto() {
        CarDto carDto = new CarDto();
        carDto.setId(1L);
        carDto.setName("Toyota Corolla");
        carDto.setEngineName("v4 120HP");
        carDto.setColor("Black");
        carDto.setType("Sedan");
        return carDto;
    }

    private List<CarDto> initCarDtos() {
        List<CarDto> carDtos = List.of(initCarDto(), initCarDto());
        carDtos.get(1).setId(2L);
        return carDtos;
    }

    private PostDto initPostDto(CarDto carDto, UserDto userDto) {
        PostDto postDto = new PostDto();
        postDto.setId(2L);
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

    private List<PostDto> initPostDtos(CarDto carDto, UserDto userDto) {
        List<PostDto> postDtos = List.of(initPostDto(carDto, userDto), initPostDto(carDto, userDto));
        postDtos.get(1).setId(2L);
        return postDtos;
    }

    @Test
    public void whenGetAllThenReturnListPostDtos() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        var expected = initPostDtos(carDto, userDto);
        when(postService.findAll(userDto)).thenReturn(expected);

        var model = new ConcurrentModel();
        var view = postController.getAll(model, userDto);
        var actual = model.getAttribute("postDtos");

        assertThat(view).isEqualTo("posts/list");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenGetCreationPageThenReturnCarDtosCreatePage() {
        var userDto = initUserDto();
        var carDtos = initCarDtos();
        when(carService.findAllByUser(userDto)).thenReturn(carDtos);

        var model = new ConcurrentModel();
        var view = postController.getCreationPage(model, userDto);
        var actual = model.getAttribute("carDtos");

        assertThat(view).isEqualTo("posts/create");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(carDtos);
    }

    @Test
    public void whenGetCreationPageUserHasntAnyCarThenReturnErrorMessage() {
        var userDto = initUserDto();
        when(carService.findAllByUser(userDto)).thenReturn(new ArrayList<>());

        var model = new ConcurrentModel();
        var view = postController.getCreationPage(model, userDto);
        var expectedMessage = "You haven't any car! First create your car";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenPostCreateThenSuccessfulAndRedirectCars() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        var postDto = initPostDto(carDto, userDto);
        var multipartFiles = new MultipartFile[2];
        var files = List.of(new File(), new File());
        when(fileService.createFilesFromMultipartFiles(multipartFiles)).thenReturn(files);
        when(postService.create(postDto, carDto, files)).thenReturn(postDto);

        var model = new ConcurrentModel();
        var view = postController.create(postDto, carDto, multipartFiles, model);

        assertThat(view).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenGetByIdThenReturnPostDtoCarDtoFileIdsPriceHistories() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        var postDto = initPostDto(carDto, userDto);
        var fileIds = List.of(1L, 10L);
        var priceHistoryDtos = List.of(new PriceHistoryDto(), new PriceHistoryDto());
        priceHistoryDtos.get(0).setId(1L);
        priceHistoryDtos.get(1).setId(2L);

        when(postService.findById(postDto.getId(), userDto)).thenReturn(Optional.of(postDto));
        when(carService.findById(postDto.getCarId())).thenReturn(Optional.of(carDto));
        when(fileService.getFileIdsByPostId(postDto.getId())).thenReturn(fileIds);
        when(priceHistoryService.findAllByPostId(postDto.getId(), userDto)).thenReturn(priceHistoryDtos);

        var model = new ConcurrentModel();
        var view = postController.getById(model, postDto.getId(), userDto);
        var actualPostDto = model.getAttribute("postDto");
        var actualCarDto = model.getAttribute("carDto");
        var actualFileIds = model.getAttribute("fileIds");
        var actualPriceHistoryDtos = model.getAttribute("priceHistories");

        assertThat(view).isEqualTo("posts/one");
        assertThat(actualPostDto).usingRecursiveAssertion().isEqualTo(postDto);
        assertThat(actualCarDto).usingRecursiveAssertion().isEqualTo(carDto);
        assertThat(actualFileIds).usingRecursiveAssertion().isEqualTo(fileIds);
        assertThat(actualPriceHistoryDtos).usingRecursiveAssertion().isEqualTo(priceHistoryDtos);
    }

    @Test
    public void whenGetByIdWithWrongPostIdThenReturnErrorMessage() {
        var userDto = initUserDto();
        when(postService.findById(3L, userDto)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = postController.getById(model, 3L, userDto);
        var expectedMessage = "Post with this Id not found!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenGetByIdWithWrongCarIdThenReturnErrorMessage() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        var postDto = initPostDto(carDto, userDto);
        when(postService.findById(postDto.getId(), userDto)).thenReturn(Optional.of(postDto));
        when(carService.findById(postDto.getCarId())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = postController.getById(model, postDto.getId(), userDto);
        var expectedMessage = "Car for this Post not found!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenGetEditByIdThenReturnCarDtoColorsTypesEditPage() {
        var carDto = initCarDto();
        var userDto = initUserDto();
        var postDto = initPostDto(carDto, userDto);
        var fileIds = List.of(1L, 10L);

        when(postService.findById(2L, userDto)).thenReturn(Optional.of(postDto));
        when(fileService.getFileIdsByPostId(postDto.getId())).thenReturn(fileIds);

        var model = new ConcurrentModel();
        var view = postController.editById(model, 2L, userDto);
        var actualPostDto = model.getAttribute("postDto");
        var actualFileIds = model.getAttribute("fileIds");

        assertThat(view).isEqualTo("posts/edit");
        assertThat(actualPostDto).usingRecursiveAssertion().isEqualTo(postDto);
        assertThat(actualFileIds).usingRecursiveAssertion().isEqualTo(fileIds);
    }

    @Test
    public void whenGetEditByWrongIdThenReturnErrorMessage() {
        var userDto = initUserDto();
        when(postService.findById(2L, userDto)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = postController.editById(model, 2L, userDto);
        var expectedMessage = "Post with this Id not found!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenPostUpdateThenRedirectPosts() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        var postDto = initPostDto(carDto, userDto);
        when(postService.update(postDto, userDto)).thenReturn(true);

        var model = new ConcurrentModel();
        var view = postController.update(postDto, userDto, new MultipartFile[0], model);

        assertThat(view).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenPostUpdateThenReturnErrorMessage() {
        var userDto = initUserDto();
        var carDto = initCarDto();
        var postDto = initPostDto(carDto, userDto);
        when(postService.update(postDto, userDto)).thenReturn(false);

        var model = new ConcurrentModel();
        var view = postController.update(postDto, userDto, new MultipartFile[0], model);
        var expectedMessage = "Update Post was unsuccessful!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenGetDeleteThenRedirectCars() {
        var userDto = initUserDto();
        when(postService.deleteById(2L, userDto)).thenReturn(true);

        var model = new ConcurrentModel();
        var view = postController.delete(model, 2L, userDto);

        assertThat(view).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenGetDeleteThenReturnErrorMessage() {
        var userDto = initUserDto();
        when(postService.deleteById(2L, userDto)).thenReturn(false);

        var model = new ConcurrentModel();
        var view = postController.delete(model, 2L, userDto);
        var expectedMessage = "Delete Post was unsuccessful!";
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
