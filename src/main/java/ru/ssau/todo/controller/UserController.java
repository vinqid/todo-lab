package ru.ssau.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.dto.UserDto;
import ru.ssau.todo.service.CustomUserDetailsService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CustomUserDetailsService userDetailsService;

    public UserController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody UserDto userDto) {
        return userDetailsService.register(userDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userDetailsService.findAll();
    }
}