package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto, Model model) {
        userDto.setId(0);
        if (userService.create(userDto).getId() == 0) {
            model.addAttribute("error", "User with this email is exist");
            return "users/register";
        }
        return "redirect:/posts";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUserDto(@ModelAttribute UserDto userDto, Model model, HttpServletRequest request) {
        var userOptional = userService.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Wrong email or password");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("userDto", userOptional.get());
        return "redirect:/posts";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

}
