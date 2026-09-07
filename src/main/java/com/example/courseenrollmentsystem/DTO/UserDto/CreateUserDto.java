package com.example.courseenrollmentsystem.DTO.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateUserDto {
    @NotNull
    private String Username;
    @NotNull
    private String Password;
    @Email
    @NotNull
    private String Email;
}
