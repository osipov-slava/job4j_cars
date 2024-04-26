package ru.job4j.cars.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "post.id", target = "id")
    @Mapping(source = "post.car.id", target = "carId")
    @Mapping(source = "carDto.name", target = "carName")
    @Mapping(source = "post.user.id", target = "userId")
    @Mapping(source = "carDto.ownerName", target = "ownerName")
    PostDto getModelFromEntity(Post post, CarDto carDto, int price);

    @Mapping(source = "carId", target = "car.id")
    @Mapping(source = "userId", target = "user.id")
    Post getEntityFromDto(PostDto postDto);

}
