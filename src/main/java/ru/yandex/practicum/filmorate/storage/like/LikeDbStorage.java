package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(int id, int userId) {
        String sql = "INSERT INTO schema.likes(film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, id, userId);
        updateRate(id);
    }

    @Override
    public void delete(int id, int userId) {
        String sql = "DELETE FROM schema.likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
        updateRate(id);
    }

    private void updateRate(int id) {
        String sqlQuery = "UPDATE schema.film f " +
                "SET rate = (SELECT COUNT(l.user_id) from schema.likes l where l.film_id = f.id)  WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
