package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreAndMpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.util.TestFilm;

import java.util.List;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {
    private final FilmDbStorage storage;
    private static Film film0;
    private static Film film1;
    private static Film film2;

    @BeforeAll
    public static void setUp() {
        film0 = TestFilm.newTestFilm(0);
        film1 = TestFilm.newTestFilm(1);
        film2 = TestFilm.newTestFilm(2);
    }

    @Order(1)
    @Test
    @DisplayName("Создание фильма")
    public void createTest() {
        film0.setId(1);
        film1.setId(2);
        film2.setId(3);
        Assertions.assertEquals(film0, storage.create(film0));
        Assertions.assertEquals(film1, storage.create(film1));
        Assertions.assertEquals(film2, storage.create(film2));
    }

    @Order(2)
    @Test
    @DisplayName("Обновление фильм")
    public void updateTest() {
        film0.setName("updateName");
        Assertions.assertEquals(film0, storage.update(film0));
    }

    @Order(3)
    @Test
    @DisplayName("Найти фильм по id")
    public void findByIdTest() {
        Assertions.assertEquals(film0, storage.findById(1));
    }

    @Order(4)
    @Test
    @DisplayName("Найти фильм по несуществующему id")
    public void findByIncorrectIdTest() {
        Assertions.assertThrows(IncorrectIdException.class, () -> storage.findById(99));
    }

    @Order(5)
    @Test
    @DisplayName("Найти все фильмы")
    public void findAllTest() {
        List<Film> ls = storage.findAll();
        Assertions.assertTrue(ls.contains(film0));
        Assertions.assertTrue(ls.contains(film1));
        Assertions.assertTrue(ls.contains(film2));
        Assertions.assertEquals(3, ls.size());
    }

    @Order(6)
    @Test
    @DisplayName("Добавление лайка фильму id=1 и id=2")
    public void addLikeTest() {
        storage.addLike(1,1);
        storage.addLike(1,2);
        storage.addLike(2,3);
        Set<Integer> set1 = storage.findById(1).getLikes();
        Set<Integer> set2 = storage.findById(2).getLikes();
        Assertions.assertTrue(set1.contains(1));
        Assertions.assertTrue(set1.contains(2));
        Assertions.assertEquals(2, set1.size());
        Assertions.assertTrue(set2.contains(3));
        Assertions.assertEquals(1, set2.size());

        Assertions.assertThrows(IncorrectIdException.class, () -> storage.addLike(3, 99),
                "Не выбросило исключение с несуществующим id юзера");
        Assertions.assertThrows(IncorrectIdException.class, () -> storage.addLike(99, 3),
                "Не выбросило исключение с несуществующим id фильма");
    }

    @Order(7)
    @Test
    @DisplayName("Удаление лайка фильму id=1")
    public void deleteLikeTest() {
        storage.addLike(1, 3);
        storage.deleteLike(1, 3);
        Set<Integer> set = storage.findById(1).getLikes();
        Assertions.assertFalse(set.contains(3), "Лайк не удалился");
        Assertions.assertThrows(IncorrectIdException.class, () -> storage.deleteLike(1,99),
                "Не выбросило исключение с несуществующим id юзера");
        Assertions.assertThrows(IncorrectIdException.class, () -> storage.deleteLike(99,3),
                "Не выбросило исключение с несуществующим id фильма");
    }

    @Order(8)
    @Test
    @DisplayName("Выгрузить все жанры")
    public void findAllGenresTest() {
        List<GenreAndMpa> ls = storage.findAllGenreOrMpa("genre");
        GenreAndMpa comedy = GenreAndMpa.builder().id(1).name("Комедия").build();
        GenreAndMpa action = GenreAndMpa.builder().id(6).name("Боевик").build();
        Assertions.assertTrue(ls.contains(comedy));
        Assertions.assertTrue(ls.contains(action));
        Assertions.assertEquals(6, ls.size());
    }

    @Order(9)
    @Test
    @DisplayName("Выгрузить все рейтинги")
    public void findAllMpaTest() {
        List<GenreAndMpa> ls = storage.findAllGenreOrMpa("mpa");
        GenreAndMpa g = GenreAndMpa.builder().id(1).name("G").build();
        GenreAndMpa nc17 = GenreAndMpa.builder().id(5).name("NC-17").build();
        Assertions.assertTrue(ls.contains(g));
        Assertions.assertTrue(ls.contains(nc17));
        Assertions.assertEquals(5, ls.size());
    }

    @Order(10)
    @Test
    @DisplayName("Выгрузить жанр по id")
    public void findGenreByIdTest() {
        GenreAndMpa comedy = GenreAndMpa.builder().id(1).name("Комедия").build();
        Assertions.assertEquals(comedy, storage.findByIdGenreOrMpa(1,"genre"));
        Assertions.assertThrows(IncorrectIdException.class, () -> storage.findByIdGenreOrMpa(99,"genre"),
                "Не выбросило исключение с несуществующим id жанра");
    }

    @Order(11)
    @Test
    @DisplayName("Выгрузить рейтинг по id")
    public void findMpaByIdTest() {
        GenreAndMpa g = GenreAndMpa.builder().id(1).name("G").build();
        Assertions.assertEquals(g, storage.findByIdGenreOrMpa(1,"mpa"));
        Assertions.assertThrows(IncorrectIdException.class, () -> storage.findByIdGenreOrMpa(99,"mpa"),
                "Не выбросило исключение с несуществующим id рейтинга");
    }
}
