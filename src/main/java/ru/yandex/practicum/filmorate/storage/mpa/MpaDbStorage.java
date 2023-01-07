package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * FROM schema.mpa";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeMpa(rs)));
    }

    @Override
    public Mpa findById(int id) {
        try {
            String sql = "SELECT * FROM schema.mpa WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeMpa(rs)), id);
        } catch (Exception e) {
            throw new IncorrectIdException();
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
