package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;

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
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(int id) {
        containsFilms(id);
        return films.get(id);
    }

    @Override
    public List<Film> findPopular() {
        return films.values()
                .stream()
                .sorted(Comparator.comparing((Film o) -> o.getLikes().size()).reversed())
                .collect(Collectors.toList());
    }

    private void containsFilms(int id) {
        if(!films.containsKey(id)) throw new IncorrectIdException();
    }
}
