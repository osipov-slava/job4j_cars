package ru.job4j.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.service.PostService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostFilterControllerTest {

    private PostFilterController postFilterController;

    private PostService postService;

    @BeforeEach
    public void initServices() {
        postService = mock(PostService.class);
        postFilterController = new PostFilterController(postService);
    }

    private UserDto initUserDto() {
        UserDto userDto = new UserDto();
        userDto.setTimezone("UTC");
        userDto.setName("User Name");
        userDto.setPassword("password");
        userDto.setEmail("mail@mail.com");
        userDto.setLogin("user");
        return userDto;
    }

    private List<PostDto> initPostDtos() {
        List<PostDto> postDtos = List.of(new PostDto(), new PostDto());
        postDtos.get(0).setDescription("Post1");
        postDtos.get(1).setDescription("Post2");
        return postDtos;
    }

    @Test
    public void whenGetActiveThenReturnListPostDtos() {
        UserDto userDto = initUserDto();
        var expected = initPostDtos();
        when(postService.findActive(userDto)).thenReturn(expected);

        var model = new ConcurrentModel();
        var view = postFilterController.getActive(model, userDto);
        var actual = model.getAttribute("postDtos");

        assertThat(view).isEqualTo("posts/list");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenGetInactiveThenReturnListPostDtos() {
        UserDto userDto = initUserDto();
        var expected = initPostDtos();
        when(postService.findInactive(userDto)).thenReturn(expected);

        var model = new ConcurrentModel();
        var view = postFilterController.getInactive(model, userDto);
        var actual = model.getAttribute("postDtos");

        assertThat(view).isEqualTo("posts/list");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenGetWithPhotosThenReturnListPostDtos() {
        UserDto userDto = initUserDto();
        var expected = initPostDtos();
        when(postService.findWithPhotos(userDto)).thenReturn(expected);

        var model = new ConcurrentModel();
        var view = postFilterController.getWithPhotos(model, userDto);
        var actual = model.getAttribute("postDtos");

        assertThat(view).isEqualTo("posts/list");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    public void whenGetLastDayThenReturnListPostDtos() {
        UserDto userDto = initUserDto();
        var expected = initPostDtos();
        when(postService.findLastDay(userDto)).thenReturn(expected);

        var model = new ConcurrentModel();
        var view = postFilterController.getLastDay(model, userDto);
        var actual = model.getAttribute("postDtos");

        assertThat(view).isEqualTo("posts/list");
        assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
    }

}
