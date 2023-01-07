package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM schema.genre";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeGenre(rs)));
    }

    @Override
    public Genre findById(int id) {
        try {
            String sql = "SELECT * FROM schema.genre WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeGenre(rs)), id);
        } catch (Exception e) {
            throw new IncorrectIdException();
        }
    }

    public static Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
