package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.Login;

import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    private String name;
    @NotNull
    @Email
    private String email;
    @NotBlank
    @Login
    private String login;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
