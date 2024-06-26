package ru.job4j.cars.dto;

import lombok.Data;

@Data
public class PostDto {

    private Long id;

    private String description;

    private String created;

    private Boolean isActive = false;

    private Long carId;

    private String carName;

    private Long userId;

    private String ownerName;

    private long price;

    private Long priceHistoryId;

}
