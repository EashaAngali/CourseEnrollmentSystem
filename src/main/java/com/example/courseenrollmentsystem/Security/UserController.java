package com.example.courseenrollmentsystem.Security;

import com.example.courseenrollmentsystem.DTO.UserDto.CreateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/User")
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<String> RegisterUser(
            CreateUserDto createUserDto) {
        return ResponseEntity.ok(
                userService.registerUser(
                        createUserDto));
    }
}
