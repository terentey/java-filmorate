package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.UserMapper.makeUser;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll(int id) {
        String sql = "SELECT * FROM schema.users WHERE id IN(SELECT friend_id FROM schema.user_friend WHERE user_id = ?)";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), id);
    }

    @Override
    public List<User> findCommon(int id, int otherId) {
        String sql = "SELECT * FROM schema.users u, schema.user_friend f, schema.user_friend o " +
                "WHERE u.id = f.friend_id AND u.id = o.friend_id AND f.user_id = ? AND o.user_id = ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), id, otherId);
    }

    @Override
    public void create(int id, int friendId) {
        try {
            jdbcTemplate.update("INSERT INTO schema.user_friend(user_id, friend_id) VALUES (?, ?)", id, friendId);
        } catch (Exception e) {
            throw new IncorrectIdException();
        }
    }

    @Override
    public void delete(int id, int friendId) {
        String sql = "DELETE FROM schema.user_friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }
}
