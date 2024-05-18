package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.UserMapper;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.HbnOwnerRepository;
import ru.job4j.cars.repository.HbnUserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private final HbnUserRepository userRepository;

    private final HbnOwnerRepository ownerRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        var user = userMapper.getEntityFromDto(userDto);
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
        var user = optionalUser.get();
        var optionalOwner = ownerRepository.findByUserId(user.getId());
        var owner = new Owner();
        if (optionalOwner.isPresent()) {
            owner = optionalOwner.get();
        }
        var userDto = userMapper.getModelFromEntities(user, owner);

        return Optional.of(userDto);
    }

}
