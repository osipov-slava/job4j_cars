package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.HbnOwnerRepository;
import ru.job4j.cars.repository.HbnUserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private final HbnUserRepository userRepository;

    private final HbnOwnerRepository ownerRepository;

    @Override
    public UserDto save(UserDto userDto) {
        var user = new User();
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        userRepository.create(user);
        userDto.setId(user.getId());

        if (!userDto.getName().isEmpty()) {
            var owner = new Owner();
            owner.setUser(user);
            owner.setName(userDto.getName());
            ownerRepository.create(owner);
        }
        return userDto;
    }

    @Override
    public Optional<UserDto> findByEmailAndPassword(String email, String password) {
        var optionalUser = userRepository.findByEmailAndPassword(email, password);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        var userDto = new UserDto();
        var user = optionalUser.get();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());

        var optionalOwner = ownerRepository.findByUserId(user.getId());
        if (optionalOwner.isPresent()) {
            var owner = optionalOwner.get();
            userDto.setName(owner.getName());
        } else {
            userDto.setName("");
        }
        return Optional.of(userDto);
    }

}
