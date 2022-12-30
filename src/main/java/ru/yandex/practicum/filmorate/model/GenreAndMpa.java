package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreAndMpa {
    Integer id;
    String name;
}
