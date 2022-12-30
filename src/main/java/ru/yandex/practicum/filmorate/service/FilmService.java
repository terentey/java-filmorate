package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreAndMpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage) {
        this.storage = storage;
    }

    public Film create(Film film) {
        return storage.create(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(int id) {
        return storage.findById(id);
    }
    public void addLike(int id, int userId) {
        storage.addLike(id, userId);
    }

    public List<Film> findPopular(Integer count) {
        return storage.findPopular(count);
    }

    public List<GenreAndMpa> findAllGenreOrMpa(String genreOrMpa) {
        return storage.findAllGenreOrMpa(genreOrMpa);
    }

    public GenreAndMpa findByIdGenreOrMpa(int id, String genreOrMpa) {
        return storage.findByIdGenreOrMpa(id, genreOrMpa);
    }

    public void deleteLike(int id, int userId) {
        storage.deleteLike(id, userId);
    }
}
