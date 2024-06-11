package ru.job4j.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    private UserService userService;

    private UserController userController;

    @Mock
    private MockHttpSession mockHttpSession;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        request = mock(HttpServletRequest.class);
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

    @Test
    public void whenRequestRegisterPage() {
        var model = new ConcurrentModel();
        var view = userController.getRegistrationPage(model);

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenPostRegisterUserThenRedirectPostsPage() {
        var expectedUserDto = initUserDto();
        expectedUserDto.setId(1);
        var newUserDto = initUserDto();
        var userArgumentCaptor = ArgumentCaptor.forClass(UserDto.class);
        when(userService.create(userArgumentCaptor.capture())).thenReturn(expectedUserDto);

        var model = new ConcurrentModel();
        var view = userController.register(newUserDto, model);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(actualUser).isEqualTo(newUserDto);
        assertThat(view).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenPostRegisterExistedUserThenReturnErrorMessage() {
        UserDto userDto = initUserDto();
        var expectedMessage = "User with this email is exist";
        when(userService.create(userDto)).thenReturn(userDto);

        var model = new ConcurrentModel();
        var view = userController.register(userDto, model);
        var actualMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/register");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestLoginPage() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginThenAddUserDtoToSession() {
        UserDto userDto = initUserDto();
        when(userService.findByEmailAndPassword(userDto.getEmail(),
                userDto.getPassword())).thenReturn(Optional.of(userDto));
        when(request.getSession()).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute("userDto")).thenReturn(userDto);

        var model = new ConcurrentModel();
        var view = userController.loginUserDto(userDto, model, request);
        var actualUserDto = mockHttpSession.getAttribute("userDto");

        assertThat(view).isEqualTo("redirect:/posts");
        assertThat(actualUserDto).isEqualTo(userDto);
    }

    @Test
    public void whenLoginWithInvalidThenReturnError() {
        UserDto userDto = initUserDto();
        var expectedMessage = "Wrong email or password";
        when(userService.findByEmailAndPassword(userDto.getEmail(),
                userDto.getPassword())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.loginUserDto(userDto, model, request);
        var actualMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenLogout() {
        var view = userController.logout(mockHttpSession);

        assertThat(view).isEqualTo("redirect:/users/login");
    }

}
