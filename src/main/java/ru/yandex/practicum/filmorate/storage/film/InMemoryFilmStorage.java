package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreAndMpa;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Integer id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        id++;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        containsFilms(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return List.copyOf(films.values());
    }

    @Override
    public Film findById(int id) {
        containsFilms(id);
        return films.get(id);
    }

    @Override
    public List<Film> findPopular(int count) {
        return films.values()
                .stream()
                .sorted(Comparator.comparing((Film o) -> o.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(int id, int userId) {
        findById(id).getLikes().add(userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        Film film = findById(id);
        if(!film.getLikes().contains(userId)) throw new IncorrectIdException();
        film.getLikes().remove(userId);
    }

    @Override
    public List<GenreAndMpa> findAllGenreOrMpa(String str) {
        return null;
    }

    @Override
    public GenreAndMpa findByIdGenreOrMpa(int id, String str) {
        return null;
    }

    private void containsFilms(int id) {
        if(!films.containsKey(id)) throw new IncorrectIdException();
    }
}
