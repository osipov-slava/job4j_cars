package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.mapstruct.CarMapper;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.EngineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleCarService implements CarService {

    private final CarRepository carRepository;

    private final EngineRepository engineRepository;

    private final CarMapper carMapper;

    @Override
    public CarDto create(CarDto carDto) {
        var car = carRepository.create(carMapper.getEntityFromDto(carDto));
        return carMapper.getModelFromEntity(car);
    }

    @Override
    public Optional<CarDto> findById(Long id) {
        Optional<CarDto> optionalCarDto = Optional.empty();
        var optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            optionalCarDto = Optional.of(carMapper.getModelFromEntity(optionalCar.get()));
        }
        return optionalCarDto;
    }

    @Override
    public List<CarDto> findAll() {
        List<Car> cars = carRepository.findAll();
        List<CarDto> carDtos = new ArrayList<>();
        for (Car car : cars) {
            carDtos.add(carMapper.getModelFromEntity(car));
        }
        return carDtos;
    }

    @Override
    public List<CarDto> findAllByUser(UserDto userDto) {
        List<Car> cars = carRepository.findAllByOwnerId(userDto.getOwnerId());
        List<CarDto> carDtos = new ArrayList<>();
        for (Car car : cars) {
            carDtos.add(carMapper.getModelFromEntity(car));
        }
        return carDtos;
    }

    /**
     * @param carDto
     * @param userDto - current user
     * @return 'false' if car.id is invalid or User hasn't access to record in case change Owner
     */
    @Override
    public boolean update(CarDto carDto, UserDto userDto) {
        var carOptional = carRepository.findById(carDto.getId());
        if (carOptional.isPresent()) {
            if (carOptional.get().getOwner().getId() != userDto.getOwnerId()) {
                return false;
            }
        } else {
            return false;
        }
        var engine = new Engine(carDto.getEngineId(), carDto.getEngineName());
        engineRepository.update(engine);
        return carRepository.update(carMapper.getEntityFromDto(carDto));
    }

    @Override
    public boolean deleteById(Long id, UserDto userDto) {
        return carRepository.delete(id, userDto.getOwnerId());
    }

}
