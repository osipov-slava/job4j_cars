package ru.job4j.cars.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "owner.id", target = "ownerId")
    UserDto getModelFromEntities(User user, Owner owner);

    User getEntityFromDto(UserDto userDto);

}
