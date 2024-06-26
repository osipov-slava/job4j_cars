package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.dto.UserDto;
import ru.job4j.cars.service.UserService;
import ru.job4j.cars.util.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("timezones", Utils.getZonesIds());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto, Model model) {
        try {
            userService.create(userDto);
        } catch (Exception e) {
            var message = "Create User was unsuccessful! Possible user with this email is exist. Try again";
            log.error(message, e);
            model.addAttribute("error", message);
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
        var userDtoOptional = userService.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        if (userDtoOptional.isEmpty()) {
            model.addAttribute("error", "Wrong email or password");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("userDto", userDtoOptional.get());
        return "redirect:/posts";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

}
