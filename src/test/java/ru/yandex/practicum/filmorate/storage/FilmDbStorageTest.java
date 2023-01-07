package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.util.JdbcTest;
import ru.yandex.practicum.filmorate.util.TestFilm;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {
    private static final JdbcTemplate jdbc = new JdbcTemplate(JdbcTest.myDataSource());
    private final FilmStorage storage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private static Film film0;
    private static Film film1;
    private static Film film2;

    @BeforeAll
    public static void setUp() {
        film0 = TestFilm.newTestFilm(0);
        film1 = TestFilm.newTestFilm(1);
        film2 = TestFilm.newTestFilm(2);
    }

    @AfterAll
    public static void restartDb() {
        JdbcTest.restartDb();
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
        likeStorage.create(1,1);
        likeStorage.create(1,2);
        likeStorage.create(2,3);

        Set<Integer> set1 = findLikes(1);
        Set<Integer> set2 = findLikes(2);

        Assertions.assertTrue(set1.contains(1));
        Assertions.assertTrue(set1.contains(2));
        Assertions.assertEquals(2, set1.size());
        Assertions.assertTrue(set2.contains(3));
        Assertions.assertEquals(1, set2.size());
    }

    @Order(7)
    @Test
    @DisplayName("Удаление лайка фильму id=1")
    public void deleteLikeTest() {
        likeStorage.create(1, 3);
        likeStorage.delete(1, 3);
        Set<Integer> set = findLikes(1);

        Assertions.assertFalse(set.contains(3), "Лайк не удалился");
    }

    @Order(8)
    @Test
    @DisplayName("Выгрузить все жанры")
    public void findAllGenresTest() {
        List<Genre> ls = genreStorage.findAll();
        Genre comedy = Genre.builder().id(1).name("Комедия").build();
        Genre action = Genre.builder().id(6).name("Боевик").build();
        Assertions.assertTrue(ls.contains(comedy));
        Assertions.assertTrue(ls.contains(action));
        Assertions.assertEquals(6, ls.size());
    }

    @Order(9)
    @Test
    @DisplayName("Выгрузить все рейтинги")
    public void findAllMpaTest() {
        List<Mpa> ls = mpaStorage.findAll();
        Mpa g = Mpa.builder().id(1).name("G").build();
        Mpa nc17 = Mpa.builder().id(5).name("NC-17").build();
        Assertions.assertTrue(ls.contains(g));
        Assertions.assertTrue(ls.contains(nc17));
        Assertions.assertEquals(5, ls.size());
    }

    @Order(10)
    @Test
    @DisplayName("Выгрузить жанр по id")
    public void findGenreByIdTest() {
        Genre comedy = Genre.builder().id(1).name("Комедия").build();
        Assertions.assertEquals(comedy, genreStorage.findById(1));
        Assertions.assertThrows(IncorrectIdException.class, () -> genreStorage.findById(99),
                "Не выбросило исключение с несуществующим id жанра");
    }

    @Order(11)
    @Test
    @DisplayName("Выгрузить рейтинг по id")
    public void findMpaByIdTest() {
        Mpa g = Mpa.builder().id(1).name("G").build();
        Assertions.assertEquals(g, mpaStorage.findById(1));
        Assertions.assertThrows(IncorrectIdException.class, () -> mpaStorage.findById(99),
                "Не выбросило исключение с несуществующим id рейтинга");
    }

    private Set<Integer> findLikes(int filmId) {
        String sql = "SELECT user_id FROM schema.likes WHERE film_id = ?";
        return new HashSet<>(jdbc.query(sql, ((rs, rowNum) -> rs.getInt("user_id")), filmId));
    }
}
