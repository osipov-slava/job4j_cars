package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.service.PostService;

@Controller
@RequestMapping("/posts/filter")
@AllArgsConstructor
@SessionAttributes("userDto")
public class PostFilterController {

    private final PostService postService;

    @GetMapping("/active")
    public String getActive(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("postDtos", postService.findActive(userDto));
        return "posts/list";
    }

    @GetMapping("/inactive")
    public String getInactive(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("postDtos", postService.findInactive(userDto));
        return "posts/list";
    }

    @GetMapping("/withPhotos")
    public String getWithPhotos(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("postDtos", postService.findWithPhotos(userDto));
        return "posts/list";
    }

    @GetMapping("/lastDay")
    public String getLastDay(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("postDtos", postService.findLastDay(userDto));
        return "posts/list";
    }

}
