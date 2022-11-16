package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.Login;

import java.time.LocalDate;

@Data
public class User {
    Integer id;
    String name;
    @NotNull
    @Email
    String email;
    @NotBlank
    @Login
    String login;
    @NotNull
    @Past
    LocalDate birthday;
}
