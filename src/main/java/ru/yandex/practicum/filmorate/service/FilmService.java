package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private int DEFAULT_SIZE = 10;
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public Film createFilm(Film film) {
        return storage.create(film);
    }

    public Film updateFilm(Film film) {
        return storage.update(film);
    }

    public Collection<Film> findAllFilm() {
        return storage.findAll();
    }

    public Film findFilmById(int id) {
        return storage.findById(id);
    }
    public void addLike(int id, int userId) {
        storage.findById(id).getLikes().add(userId);
    }

    public Collection<Film> findPopular(Integer count) {
        List<Film> films = storage.findPopular();
        if(count == null) {
            if(films.size() > DEFAULT_SIZE) return films.subList(0, DEFAULT_SIZE);
            else return films;
        }
        else if(films.size() > count) return films.subList(0, count);
        else return films;
    }

    public void deleteLike(int id, int userId) {
        if(!findFilmById(id).getLikes().contains(userId)) throw new IncorrectIdException();
        findFilmById(id).getLikes().remove(userId);
    }
}
