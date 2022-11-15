package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Integer id = 0;
    Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        id++;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        if(!films.containsKey(film.getId())) throw new IncorrectIdException();
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> find() {
        return films.values();
    }
}
