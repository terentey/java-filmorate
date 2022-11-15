package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ValidatorTest;

import java.time.LocalDate;

public class FilmTest {
    private final static String NAME = "nisi eiusmod";
    private final static String DESCRIPTION = "adipisicing";
    private final static Integer DURATION = 100;
    private final static LocalDate RELEASE_DATA = LocalDate.of(1967,3,25);

    private Film film;

    @BeforeEach
    public void setUp() {
        film = new Film();
    }

    @Test
    @DisplayName("Создание корректного фильма")
    public void createFilm() {
        setUpFilm(NAME, DESCRIPTION, DURATION, RELEASE_DATA);
        Assertions.assertTrue(ValidatorTest.getViolations(film).isEmpty());
    }

    @Test
    @DisplayName("Создание фильма с некорректным названием")
    public void createFilmWithFailName() {
        setUpFilm("", DESCRIPTION, DURATION, RELEASE_DATA);
        Assertions.assertFalse(ValidatorTest.getViolations(film).isEmpty());
    }

    @Test
    @DisplayName("Создание фильма с некорректным описанием")
    public void createFilmWithFailDescription() {
        String description = "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
                "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                "который за время «своего отсутствия», стал кандидатом Коломбани.";
        setUpFilm(NAME, description, DURATION, RELEASE_DATA);
        Assertions.assertFalse(ValidatorTest.getViolations(film).isEmpty());
    }

    @Test
    @DisplayName("Создание фильма с отрицательной продолжительностью")
    public void createFilmWithFailDuration() {
        setUpFilm(NAME, DESCRIPTION, -200, RELEASE_DATA);
        Assertions.assertFalse(ValidatorTest.getViolations(film).isEmpty());
    }

    @Test
    @DisplayName("Создание фильма с некорректной датой релиза")
    public void createFilmWithFailReleaseDate() {
        setUpFilm(NAME, DESCRIPTION, DURATION, LocalDate.of(1867,3,25));
        Assertions.assertFalse(ValidatorTest.getViolations(film).isEmpty());
    }

    private void setUpFilm(String name, String description, Integer duration, LocalDate releaseDate) {
        film.setName(name);
        film.setDescription(description);
        film.setDuration(duration);
        film.setReleaseDate(releaseDate);
    }
}
