package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

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

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(int id) {
        return storage.findById(id);
    }
    public void addLike(int id, int userId) {
        storage.findById(id).getLikes().add(userId);
    }

    public Collection<Film> findPopular(String count) {
        return storage.findPopular(Integer.parseInt(count));
    }

    public void deleteLike(int id, int userId) {
        if(!findById(id).getLikes().contains(userId)) throw new IncorrectIdException();
        findById(id).getLikes().remove(userId);
    }
}
