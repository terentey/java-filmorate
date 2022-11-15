package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import java.time.LocalDate;

@Data
public class Film {
    Integer id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @ReleaseDate
    LocalDate releaseDate;
    @Positive
    Integer duration;
}
