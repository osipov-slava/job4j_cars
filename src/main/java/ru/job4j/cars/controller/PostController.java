package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.CarDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.File;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.FileService;
import ru.job4j.cars.service.PostService;
import ru.job4j.cars.service.PriceHistoryService;

import java.util.List;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
@SessionAttributes("userDto")
public class PostController {

    private final PostService postService;

    private final FileService fileService;

    private final CarService carService;

    private final PriceHistoryService priceHistoryService;

    @GetMapping
    public String getAll(Model model, @SessionAttribute UserDto userDto) {
        model.addAttribute("postDtos", postService.findAll(userDto));
        return "posts/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, @SessionAttribute UserDto userDto) {
        var carDtos = carService.findAllByUser(userDto);
        if (carDtos.isEmpty()) {
            model.addAttribute("message", "You haven't any car! First create your car");
            return "errors/404";
        }
        model.addAttribute("carDtos", carDtos);
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute PostDto postDto,
                         @ModelAttribute CarDto carDto,
                         @RequestParam MultipartFile[] multipartFiles,
                         Model model) {
        List<File> files = fileService.createFilesFromMultipartFiles(multipartFiles);

        var idCreatedPost = postService.create(postDto, carDto, files).getId();
        if (idCreatedPost == 0) {
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
        var carOptional = carService.findById(postOptional.get().getCarId());
        if (carOptional.isEmpty()) {
            model.addAttribute("message", "Car for this Post not found!");
            return "errors/404";
        }
        model.addAttribute("postDto", postOptional.get());
        model.addAttribute("carDto", carOptional.get());
        model.addAttribute("fileIds", fileService.getFileIdsByPostId(postOptional.get().getId()));
        model.addAttribute("priceHistories", priceHistoryService.findAllByPostId(id, userDto));
        return "posts/one";
    }

    @GetMapping("update/{id}")
    public String editById(Model model, @PathVariable int id, @SessionAttribute UserDto userDto) {
        var postOptional = postService.findById(id, userDto);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "Post with this Id not found!");
            return "errors/404";
        }
        model.addAttribute("postDto", postOptional.get());
        model.addAttribute("fileIds", fileService.getFileIdsByPostId(postOptional.get().getId()));
        return "posts/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute PostDto postDto,
                         @SessionAttribute UserDto userDto,
                         @RequestParam MultipartFile[] multipartFiles,
                         Model model) {
        fileService.updateFilesFromMultipartFiles(multipartFiles, postDto);

        if (!postService.update(postDto, userDto)) {
            model.addAttribute("message", "Update Post was unsuccessful!");
            return "errors/404";
        }
        return "redirect:/posts";
    }

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
