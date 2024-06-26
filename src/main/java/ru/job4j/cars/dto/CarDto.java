package ru.job4j.cars.dto;

import lombok.Data;

@Data
public class CarDto {

    private Long id;

    private String name;

    private Integer typeId;

    private String type;

    private Integer colorId;

    private String color;

    private Long engineId;

    private String engineName;

    private Long ownerId;

    private String ownerName;

}
