package ru.job4j.cars.dto;

import lombok.Data;

@Data
public class PostDto {

    private int id;

    private String description;

    private String created;

    private int carId;

    private String carName;

    private int userId;

    private String ownerName;

    private long price;

    private int priceHistoryId;

}
