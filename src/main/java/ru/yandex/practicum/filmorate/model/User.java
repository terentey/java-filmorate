package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.Login;

import java.time.LocalDate;

@Data
public class User {
    Integer id;
    @NotNull
    @Email
    String email;
    @NotBlank
    @Login
    String login;
    String name;
    @Past
    LocalDate birthday;
}
