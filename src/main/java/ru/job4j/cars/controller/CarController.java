package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.ColorService;
import ru.job4j.cars.service.TypeService;

@Controller
@RequestMapping("/cars")
@AllArgsConstructor
@SessionAttributes("userDto")
@Slf4j
public class CarController {

    private final CarService carService;

    private final ColorService colorService;

    private final TypeService typeService;

    @GetMapping
    public String getAllByUser(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("carDtos", carService.findAllByUser(userDto));
        return "cars/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("types", typeService.findAll());
        return "cars/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CarDto carDto, Model model, @SessionAttribute UserDto userDto) {
        carDto.setOwnerId(userDto.getOwnerId());
        try {
            carService.create(carDto);
        } catch (Exception e) {
            var message = "Creation Car was unsuccessful!";
            log.error(message, e);
            model.addAttribute("message", message);
            return "errors/404";
        }
        return "redirect:/cars";

    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable Long id) {
        var carOptional = carService.findById(id);
        if (carOptional.isEmpty()) {
            model.addAttribute("message", "Car with this Id not found!");
            return "errors/404";
        }
        model.addAttribute("carDto", carOptional.get());
        return "cars/one";
    }

    @GetMapping("update/{id}")
    public String editById(Model model, @PathVariable Long id) {
        var carOptional = carService.findById(id);
        if (carOptional.isEmpty()) {
            model.addAttribute("message", "Car with this Id not found!");
            return "errors/404";
        }
        model.addAttribute("carDto", carOptional.get());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("types", typeService.findAll());
        return "cars/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CarDto carDto,
                         @SessionAttribute UserDto userDto,
                         Model model) {
        try {
            if (!carService.update(carDto, userDto)) {
                throw new Exception("carService.update() returned 'false'");
            }
        } catch (Exception e) {
            var message = "Update Car was unsuccessful!";
            log.error(message, e);
            model.addAttribute("message", message);
            return "errors/404";
        }
        return "redirect:/cars";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Long id, @SessionAttribute UserDto userDto) {
        try {
            if (!carService.deleteById(id, userDto)) {
                throw new Exception("carService.deleteById() returned 'false'");
            }
        } catch (Exception e) {
            var message = "Delete Car was unsuccessful!";
            log.error(message, e);
            model.addAttribute("message", message);
            return "errors/404";
        }
        return "redirect:/cars";
    }

}
