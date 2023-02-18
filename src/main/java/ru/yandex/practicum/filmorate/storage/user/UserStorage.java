package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);
    User update(User user);
    List<User> findAll();
    Optional<User> findById(int id);
    void isExist(int id);
}
