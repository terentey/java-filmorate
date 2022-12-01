package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
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
        storage.findById(id).getLikes().add(userId);
    }

    public List<Film> findPopular(Integer count) {
        return storage.findPopular(count);
    }

    public void deleteLike(int id, int userId) {
        Film film = findById(id);
        if(!film.getLikes().contains(userId)) throw new IncorrectIdException();
        film.getLikes().remove(userId);
    }
}
