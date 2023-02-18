package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, LikeStorage likeStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmStorage.isExist(film.getId());
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(int id) {
        return filmStorage.findById(id);
    }
    public void addLike(int id, int userId) {
        filmStorage.isExist(id);
        userStorage.isExist(userId);
        likeStorage.create(id, userId);
    }

    public List<Film> findPopular(Integer count) {
        return filmStorage.findPopular(count);
    }

    public void deleteLike(int id, int userId) {
        filmStorage.isExist(id);
        userStorage.isExist(userId);
        likeStorage.delete(id, userId);
    }
}
