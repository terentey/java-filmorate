package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @ReleaseDate
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
}
