package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreAndMpa;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);
    Film update(Film film);
    List<Film> findAll();
    Film findById(int id);
    List<Film> findPopular(int count);
    void addLike(int id, int userId);
    void deleteLike(int id, int userId);
    List<GenreAndMpa> findAllGenreOrMpa(String str);
    GenreAndMpa findByIdGenreOrMpa(int id, String str);
}
