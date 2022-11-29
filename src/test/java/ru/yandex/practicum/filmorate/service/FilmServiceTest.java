package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.TestFilm.newTestFilm;

public class FilmServiceTest {
    private static Film film1;
    private static Film film2;
    private static Film film3;
    private static Film film4;
    private static Film film5;
    private static Film film6;
    private static FilmService service = new FilmService(new InMemoryFilmStorage());

    @BeforeAll
    public static void setUp() {
        film1 = newTestFilm(1);
        film2 = newTestFilm(2);
        film3 = newTestFilm(3);
        film4 = newTestFilm(4);
        film5 = newTestFilm(5);
        film6 = newTestFilm(6);
        service.createFilm(film1);
        service.createFilm(film2);
        service.createFilm(film3);
        service.createFilm(film4);
        service.createFilm(film5);
        service.createFilm(film6);
    }

    @Test
    @DisplayName("Обновление фильма")
    public void updateFilmTest() {
        int num = 7;
        Film newFilm = newTestFilm(num);
        service.createFilm(newFilm);
        Film updateFilm = newTestFilm(8);
        updateFilm.setId(num);
        service.updateFilm(updateFilm);
        Assertions.assertEquals(service.findFilmById(num), updateFilm);
    }

    @Test
    @DisplayName("Добавление лайка фильму")
    public void addLikeTest() {
        service.addLike(3,1);
        service.addLike(3,2);

        Assertions.assertEquals(service.findFilmById(3).getLikes().size(), 2);
    }

    @Test
    @DisplayName("Удаление лайка")
    public void deleteLikeTest() {
        service.addLike(3,1);
        service.deleteLike(3,1);
        Assertions.assertEquals(service.findFilmById(3).getLikes().size(), 0);
    }

    @Test
    @DisplayName("Нахождение популярных фильмов")
    public void findPopularFilm() {
        service.addLike(4,1);
        service.addLike(4,2);
        service.addLike(4,3);
        service.addLike(4,4);
        service.addLike(4,5);

        service.addLike(2,1);
        service.addLike(2,2);

        service.addLike(5,1);
        service.addLike(5,2);
        service.addLike(5,3);


        service.addLike(6,1);
        service.addLike(6,2);
        service.addLike(6,3);
        service.addLike(6,4);

        Assertions.assertEquals(service.findPopular(4), List.of(film4, film6, film5, film2));
        Assertions.assertEquals(service.findPopular(null), List.of(film4, film6, film5, film2, film1, film3));
        Assertions.assertEquals(service.findPopular(1), List.of(film4));
    }
}
