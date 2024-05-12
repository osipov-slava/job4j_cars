package ru.job4j.cars.mapstruct;

import org.mapstruct.Mapper;
import ru.job4j.cars.dto.PriceHistoryDto;
import ru.job4j.cars.model.PriceHistory;

@Mapper(componentModel = "spring")
public interface PriceHistoryMapper {

    PriceHistoryDto getModelFromEntity(PriceHistory priceHistory);

}
