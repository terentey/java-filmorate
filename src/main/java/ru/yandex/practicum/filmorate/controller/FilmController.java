package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        return service.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        service.addLike(id, userId);
    }

    @GetMapping
    public Collection<Film> find() {
        return service.findAllFilm();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return service.findFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> findPopular(@RequestParam(required = false) Integer count) {
        return service.findPopular(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        service.deleteLike(id, userId);
    }
}
