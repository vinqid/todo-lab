package ru.ssau.todo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.dto.AuthMeDto;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public AuthMeDto getCurrentUser(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AuthMeDto(authentication.getName(), roles);
    }
}