package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.PostService;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
@SessionAttributes("userDto")
public class PostController {

    private final PostService postService;

    private final CarService carService;

    @GetMapping
    public String getAll(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("postDtos", postService.findAll(userDto));
        return "posts/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("carDtos", carService.findAllByUser(userDto));
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute PostDto postDto,
                         @ModelAttribute CarDto carDto,
                         @SessionAttribute UserDto userDto,
                         Model model) {
        if (postService.create(postDto, userDto, carDto).getId() == 0) {
            model.addAttribute("message", "Creation post was unsuccessful!");
            return "errors/404";
        }
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, @SessionAttribute UserDto userDto) {
        var postOptional = postService.findById(id, userDto);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "Post with this Id not found!");
            return "errors/404";
        }
        model.addAttribute("postDto", postOptional.get());
        return "posts/one";
    }

    @GetMapping("update/{id}")
    public String editById(Model model, @PathVariable int id, @SessionAttribute UserDto userDto) {
        var optional = postService.findById(id, userDto);
        if (optional.isEmpty()) {
            model.addAttribute("message", "Post with this Id not found!");
            return "errors/404";
        }
        model.addAttribute("postDto", optional.get());
        return "posts/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute PostDto postDto,
                         @SessionAttribute UserDto userDto,
                         Model model) {
        if (!postService.update(postDto, userDto)) {
            model.addAttribute("message", "Update Post was unsuccessful!");
            return "errors/404";
        }
        return "redirect:/posts";
    }
//
//    @GetMapping("/done/{id}")
//    public String done(Model model, @PathVariable int id, @SessionAttribute User user) {
//        if (!postService.done(id, user)) {
//            model.addAttribute("message", "Task with this Id not found!");
//            return "errors/404";
//        }
//        return "redirect:/postDtos";
//    }
//
    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id, @SessionAttribute UserDto userDto) {
        var isDeleted = postService.deleteById(id, userDto);
        if (!isDeleted) {
            model.addAttribute("message", "Delete Post with this Id was unsuccessful!");
            return "errors/404";
        }
        return "redirect:/posts";
    }
}
