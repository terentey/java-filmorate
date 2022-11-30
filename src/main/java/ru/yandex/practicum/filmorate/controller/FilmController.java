package ru.yandex.practicum.filmorate.controller;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService service;

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        return service.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Валидация пройдена.");
        return service.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        service.addLike(id, userId);
    }

    @GetMapping
    public Collection<Film> find() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> findPopular(@RequestParam(required = false, defaultValue = "10") @Positive String count) {
        return service.findPopular(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        service.deleteLike(id, userId);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidData(ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
