package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.service.PostService;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
@SessionAttributes("userDto")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String getAll(Model model, @SessionAttribute UserDto userDto) {
        return "posts/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Post post,
                         @SessionAttribute UserDto userDto,
                         Model model) {
        if (postService.create(post, userDto).getId() == 0) {
            model.addAttribute("message", "Creation post was unsuccessful!");
            return "errors/404";
        }
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, @SessionAttribute UserDto userDto) {
        var postOptional = postService.findById(id);
        if (postOptional.isEmpty()) {
            model.addAttribute("message", "Post with this Id not found!");
            return "errors/404";
        }
        model.addAttribute("post", postOptional.get());
        return "posts/one";
    }
//
//    @GetMapping("update/{id}")
//    public String editById(Model model, @PathVariable int id, @SessionAttribute User user) {
//        model.addAttribute("priorities", priorityService.findAll());
//        model.addAttribute("categories", categoryService.findAll());
//        var optional = postService.findById(id, user);
//        if (optional.isEmpty()) {
//            model.addAttribute("message", "Task with this Id not found!");
//            return "errors/404";
//        }
//        model.addAttribute("task", optional.get());
//        return "tasks/edit";
//    }
//
//    @PostMapping("/update")
//    public String update(@ModelAttribute Task task,
//                         @RequestParam(value = "category.id") List<Integer> categoryIds,
//                         Model model) {
//        if (!postService.update(task.getId(), task, categoryIds)) {
//            model.addAttribute("message", "Update task was unsuccessful!");
//            return "errors/404";
//        }
//        return "redirect:/tasks";
//    }
//
//    @GetMapping("/done/{id}")
//    public String done(Model model, @PathVariable int id, @SessionAttribute User user) {
//        if (!postService.done(id, user)) {
//            model.addAttribute("message", "Task with this Id not found!");
//            return "errors/404";
//        }
//        return "redirect:/tasks";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String delete(Model model, @PathVariable int id, @SessionAttribute User user) {
//        var isDeleted = postService.deleteById(id, user);
//        if (!isDeleted) {
//            model.addAttribute("message", "Task with this Id not found!");
//            return "errors/404";
//        }
//        return "redirect:/tasks";
//    }
}
