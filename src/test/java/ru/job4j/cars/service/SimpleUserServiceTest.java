package ru.job4j.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.UserMapper;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.OwnerRepository;
import ru.job4j.cars.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SimpleUserServiceTest {

    private UserService userService;

    private OwnerRepository ownerRepository;

    @Autowired
    private UserMapper userMapper;

    private UserRepository userRepository;

    @BeforeEach
    public void initComponents() {
        userRepository = mock(UserRepository.class);
        ownerRepository = mock(OwnerRepository.class);
        userService = new SimpleUserService(userRepository, ownerRepository, userMapper);
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

    private User initUser() {
        User user = new User();
        user.setId(1);
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        user.setLogin("user");
        user.setTimezone("UTC");
        return user;
    }

    private Owner initOwner(User user) {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setUser(user);
        owner.setName("User Name");
        return owner;
    }

    @Test
    public void whenCreateUserAndOwnerThenGetUserDtoWithIds() {
        UserDto userDto = initUserDto();
        User user = initUser();
        Owner owner = initOwner(user);

        when(userRepository.create(user)).thenReturn(user);
        when(ownerRepository.create(owner)).thenReturn(owner);

        var actual = userService.create(userDto);
        userDto.setId(1);
        userDto.setOwnerId(1);
        assertThat(actual).usingRecursiveAssertion().isEqualTo(userDto);
    }

    @Test
    public void whenFindUserByEmailAndPasswordSuccessful() {
        UserDto userDto = initUserDto();
        User user = initUser();
        Owner owner = initOwner(user);

        when(userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));
        when(ownerRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(owner));

        var actual = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        userDto.setId(1);
        userDto.setOwnerId(1);
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveAssertion().isEqualTo(userDto);
    }

    @Test
    public void whenFindUnknownUserByEmailAndPasswordThenReturnOptionalEmpty() {
        User user = initUser();
        Owner owner = initOwner(user);

        when(userRepository.findByEmailAndPassword(any(), any()))
                .thenReturn(Optional.empty());
        when(ownerRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(owner));

        var actual = userService.findByEmailAndPassword("wrong@email.com", "password");
        assertThat(actual.isEmpty()).isTrue();
    }

}
