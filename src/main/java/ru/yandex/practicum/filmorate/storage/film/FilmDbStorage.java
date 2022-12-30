package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreAndMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO schema.film(name, description, release_date, duration, mpa_id, rate) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate());
        String sqlFind = "SELECT f.id FROM SCHEMA.FILM f " +
                "LEFT JOIN (SELECT id, name mpa_name FROM schema.mpa) m ON f.mpa_id = m.id " +
                "WHERE name = ? AND description = ? AND release_date = ? " +
                "AND duration = ? AND mpa_id = ?";
        film.setId(jdbcTemplate.queryForObject(sqlFind, ((rs, rowNum) -> rs.getInt("id")), film.getName(),
                film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId()));
        updateGenres(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE schema.film SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?, rate = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        updateGenres(film);
        return findById(film.getId());
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM schema.film f " +
                "LEFT JOIN (SELECT id, name mpa_name FROM schema.mpa) m ON f.mpa_id = m.id";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public Film findById(int id) {
        try {
            String sql = "SELECT * FROM schema.film f " +
                    "LEFT JOIN (SELECT id, name mpa_name FROM schema.mpa) m ON f.mpa_id = m.id " +
                    "WHERE f.id = ?";
            return jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeFilm(rs)), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IncorrectIdException();
        }
    }

    @Override
    public List<Film> findPopular(int count) {
        String sql = "SELECT * FROM schema.film AS f " +
                "LEFT JOIN schema.likes ON f.id = likes.film_id " +
                "LEFT JOIN (SELECT id, name mpa_name FROM schema.mpa) m ON f.mpa_id = m.id " +
                "GROUP BY f.id ORDER BY COUNT(likes.film_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), count);
    }

    @Override
    public void addLike(int id, int userId) {
        String sqlFilm = "SELECT EXISTS(SELECT * FROM schema.film WHERE id = ?)";
        String sqlUser = "SELECT EXISTS(SELECT * FROM schema.users WHERE id = ?)";
        boolean isExist = jdbcTemplate.queryForObject(sqlFilm , ((rs, rowNum) -> rs.getBoolean(1)), id)
                && jdbcTemplate.queryForObject(sqlUser, ((rs, rowNum) -> rs.getBoolean(1)), userId);
        if(isExist) {
            String sql = "INSERT INTO schema.likes(film_id, user_id) VALUES(?, ?)";
            jdbcTemplate.update(sql, id, userId);
        } else throw new IncorrectIdException();
    }

    @Override
    public void deleteLike(int id, int userId) {
        if(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM schema.likes WHERE film_id = ? AND user_id = ?)",
                    ((rs, rowNum) -> rs.getBoolean(1)), id, userId)) {
            String sql = "DELETE FROM schema.likes WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(sql, id, userId);
        } else throw new IncorrectIdException();
    }

    @Override
    public List<GenreAndMpa> findAllGenreOrMpa(String str) {
        String sql = "SELECT * FROM schema." + str;
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeGenreOrMpa(rs)));
    }

    @Override
    public GenreAndMpa findByIdGenreOrMpa(int id, String str) {
        try {
            String sql = "SELECT * FROM schema." + str + " WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeGenreOrMpa(rs)), id);
        } catch (Exception e) {
            throw new IncorrectIdException();
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        Integer id = rs.getInt("id");
        film.setId(id);
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(GenreAndMpa.builder().id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name")).build());
        film.setRate(rs.getInt("rate"));
        film.setGenres(new HashSet<>(jdbcTemplate.query("SELECT genre_id AS id, name FROM schema.genre_film AS gf " +
                        "LEFT JOIN schema.genre g on g.id = gf.genre_id " +
                        "WHERE film_id = ?", ((o, rowNum) -> makeGenreOrMpa(o)), id)));
        film.setLikes(new HashSet<>(jdbcTemplate.query("SELECT user_id FROM schema.likes WHERE film_id = ?",
                ((o, rowNum) -> o.getInt("user_id")), id)));
        return film;
    }

    private GenreAndMpa makeGenreOrMpa(ResultSet rs) throws SQLException {
        return GenreAndMpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    private void updateGenres(Film film) {
        String sqlInsert = "INSERT INTO schema.genre_film(genre_id, film_id) VALUES(?, ?)";
        int id = film.getId();
        if(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM schema.genre_film WHERE film_id = ?)",
                ((rs, rowNum) -> rs.getBoolean(1)), id)) {
            if(film.getGenres() != null) {
                Set<Integer> genresFromTable = new HashSet<>(jdbcTemplate
                        .query("SELECT genre_id FROM schema.genre_film WHERE film_id = ?",
                        ((rs, rowNum) -> rs.getInt("genre_id")), id));
                Set<Integer> genres = new HashSet<>();
                film.getGenres().forEach(o -> genres.add(o.getId()));
                for(Integer genre : genres) {
                    if(!genresFromTable.contains(genre)) jdbcTemplate.update(sqlInsert, genre, id);
                }
                for(Integer genre : genresFromTable) {
                    if(!genres.contains(genre)) {
                        jdbcTemplate.update("DELETE FROM schema.genre_film WHERE genre_id = ? AND film_id = ?",
                                genre, id);
                    }
                }
            } else {
                jdbcTemplate.update("DELETE FROM schema.genre_film WHERE film_id = ?", id);
            }
        }else if(film.getGenres() != null) {
            film.getGenres().forEach(o -> jdbcTemplate
                    .update(sqlInsert, o.getId(), id));
        }
    }
}
