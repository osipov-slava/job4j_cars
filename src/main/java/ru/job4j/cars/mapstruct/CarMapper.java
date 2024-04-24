package ru.job4j.cars.mapstruct;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.model.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "car.id", target = "id")
    @Mapping(source = "car.name", target = "name")
    @Mapping(source = "car.engine.id", target = "engineId")
    @Mapping(source = "car.engine.name", target = "engineName")
    @Mapping(source = "car.owner.id", target = "ownerId")
    @Mapping(source = "car.owner.name", target = "ownerName")
    CarDto getModelFromEntity(Car car);

    @InheritInverseConfiguration
    Car getEntityFromDto(CarDto carDto);

}
