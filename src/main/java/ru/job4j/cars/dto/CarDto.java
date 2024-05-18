package ru.job4j.cars.dto;

import lombok.Data;

@Data
public class CarDto {

    private int id;

    private String name;

    private int typeId;

    private String type;

    private int colorId;

    private String color;

    private int engineId;

    private String engineName;

    private int ownerId;

    private String ownerName;

}
