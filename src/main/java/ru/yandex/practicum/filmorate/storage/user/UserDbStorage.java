package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final static String SQL_FRIENDS = "INSERT INTO schema.user_friend(user_1, user_2) VALUES (?, ?)";
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlInsert = "INSERT INTO schema.users(name, email, login, birthday) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        updateLikes(user, SQL_FRIENDS);
        String sqlFind = "SELECT * FROM SCHEMA.USERS WHERE name = ? AND email = ? AND login = ? AND birthday = ?";
        return jdbcTemplate.queryForObject(sqlFind, ((rs, rowNum) -> makeUser(rs)), user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday());
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE schema.users SET name = ?, email = ?, login = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        updateLikes(user, SQL_FRIENDS);
        return findById(user.getId()).orElseThrow(IncorrectIdException::new);
    }

    @Override
    public List<User> findAll() {
        String  sql = "SELECT * FROM schema.users";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)));
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            String sql = "SELECT * FROM schema.users WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ((rs, rowNum) -> makeUser(rs)), id));
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IncorrectIdException();
        }
    }

    @Override
    public List<User> findFriends(int id) {
        String sql = "SELECT * FROM schema.users WHERE id IN(SELECT user_2 FROM schema.user_friend WHERE user_1 = ?)";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), id);
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        String sql = "SELECT * FROM schema.users WHERE id IN(" +
                "SELECT user_2 FROM schema.user_friend WHERE user_1 = ? AND user_2 IN(" +
                "SELECT user_2 FROM schema.user_friend WHERE user_1 = ?))";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), id, otherId);
    }

    @Override
    public void addFriend(int id, int friendId) {
        try {
            jdbcTemplate.update(SQL_FRIENDS, id, friendId);
        } catch (Exception e) {
            throw new IncorrectIdException();
        }
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sql = "DELETE FROM schema.user_friend WHERE user_1 = ? AND user_2 = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    private static User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }

    private void updateLikes(User user, String sql) {
        if(user.getId() != null) {
            Integer id = user.getId();
            user.getFriends().forEach(o -> jdbcTemplate.update(sql, id, o));
        }
    }
}
