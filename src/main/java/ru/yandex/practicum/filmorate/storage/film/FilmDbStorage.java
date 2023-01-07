package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO schema.film(name, description, release_date, duration, mpa_id) " +
                "VALUES(?, ?, ?, ?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        };
        jdbcTemplate.update(creator, holder);
        film.setId(holder.getKey().intValue());
        updateGenres(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE schema.film SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        updateGenres(film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM schema.film f " +
                "LEFT JOIN (SELECT id mpa_id, name mpa_name FROM schema.mpa) m ON f.mpa_id = m.mpa_id";
        List<Film> films = jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
        setGenres(films);
        return films;
    }

    @Override
    public Film findById(int id) {
        try {
            String sql = "SELECT * FROM schema.film f " +
                    "LEFT JOIN (SELECT id, name mpa_name FROM schema.mpa) m ON f.mpa_id = m.id " +
                    "WHERE f.id = ?";
            List<Film> films = Collections
                    .singletonList(jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeFilm(rs)), id));
            setGenres(films);
            return films.get(0);
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
        List<Film> films = jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), count);
        setGenres(films);
        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        Integer id = rs.getInt("id");
        film.setId(id);
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(Mpa.builder().id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name")).build());
        film.setRate(rs.getInt("rate"));
        return film;
    }

    private void updateGenres(Film film) {
        int id = film.getId();
        String sqlInsert = String.format("INSERT INTO schema.genre_film(genre_id, film_id) VALUES(?, %d)", id);
        jdbcTemplate.update("DELETE FROM schema.genre_film WHERE film_id = ?", id);
        if(film.getGenres() != null) {
            Set<Genre> genres = new LinkedHashSet<>(film.getGenres()
                    .stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            List<Integer> ids = new ArrayList<>();
            genres.forEach(g -> ids.add(g.getId()));
            film.setGenres(genres);
            jdbcTemplate.batchUpdate(sqlInsert, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, ids.get(i));
                }

                @Override
                public int getBatchSize() {
                    return ids.size();
                }
            });
        }

    }

    @Override
    public void isExist(int id) {
        String sql = "SELECT EXISTS(SELECT * FROM schema.film WHERE id = ?)";
        if(!jdbcTemplate.queryForObject(sql , ((rs, rowNum) -> rs.getBoolean(1)), id)) {
            throw new IncorrectIdException();
        }
    }

    private void setGenres(List<Film> films) {
        List<Integer> ids = new ArrayList<>();
        Map<Integer, Set<Genre>> filmGenre;
        films.forEach(o -> ids.add(o.getId()));
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("SELECT * FROM schema.genre_film gf " +
                "LEFT JOIN schema.genre g ON gf.genre_id = g.id WHERE film_id IN (%s)", inSql);
        filmGenre = jdbcTemplate.query(sql, ids.toArray(), ((rs) -> {
            Map<Integer, Set<Genre>> map = new HashMap<>();
            while (rs.next()) {
                int film_id = rs.getInt("film_id");
                Genre genre = Genre.builder()
                        .id(rs.getInt("genre_id")).name(rs.getString("name")).build();
                if(map.containsKey(film_id)) {
                    map.get(film_id).add(genre);
                } else map.put(film_id, new HashSet<>(List.of(genre)));
            }
            return map;
        }));
        for(Film f : films) {
            int film_id = f.getId();
            if (filmGenre.containsKey(film_id)) {
                f.setGenres(filmGenre.get(film_id));
            } else f.setGenres(new HashSet<>());
        }
    }
}
