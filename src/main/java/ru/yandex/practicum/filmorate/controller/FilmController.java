package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreAndMpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final static String GENRE = "genre";
    private final static String MPA = "mpa";
    private final FilmService service;

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        return service.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        return service.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        service.addLike(id, userId);
    }

    @GetMapping("/films")
    public List<Film> find() {
        return service.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> findPopular(@RequestParam(required = false, defaultValue = "10") @Positive Integer count) {
        return service.findPopular(count);
    }

    @GetMapping("/genres")
    public List<GenreAndMpa> findAllGenres() {
        return service.findAllGenreOrMpa(GENRE);
    }
    @GetMapping("/mpa")
    public List<GenreAndMpa> findAllMpa() {
        return service.findAllGenreOrMpa(MPA);
    }

    @GetMapping("/genres/{id}")
    public GenreAndMpa findByIdGenres(@PathVariable int id) {
        return service.findByIdGenreOrMpa(id, GENRE);
    }

    @GetMapping("/mpa/{id}")
    public GenreAndMpa findByIdMpa(@PathVariable int id) {
        return service.findByIdGenreOrMpa(id, MPA);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        service.deleteLike(id, userId);
    }
}
