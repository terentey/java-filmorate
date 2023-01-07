package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.util.Users.makeUser;

@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String sql = "INSERT INTO schema.users(name, email, login, birthday) VALUES(?, ?, ?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        };
        jdbcTemplate.update(creator, holder);
        user.setId((Integer) holder.getKey());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE schema.users SET name = ?, email = ?, login = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
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
    public void isExist(int id) {
        String sql = "SELECT EXISTS(SELECT * FROM schema.users WHERE id = ?)";
        if(!jdbcTemplate.queryForObject(sql , ((rs, rowNum) -> rs.getBoolean(1)), id)) {
            throw new IncorrectIdException();
        }
    }
}
