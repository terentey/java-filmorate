package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class TestFilm {
    public static void setUpFilm(Film film, String name, String description, Integer duration, LocalDate releaseDate) {
        film.setName(name);
        film.setDescription(description);
        film.setDuration(duration);
        film.setReleaseDate(releaseDate);
    }

    public static Film newTestFilm(int num) {
        Film film = new Film();
        setUpFilm(film,
                "name_" + num,
                "description_" + num,
                100 + num,
                LocalDate.of(2000,1,1).plusDays(num));
        return film;
    }
}
