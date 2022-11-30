package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);
    User update(User user);
    Collection<User> findAll();
    Optional<User> findById(int id);
    void containsUser(int id);
    List<User> findFriends(int id);
    List<User> findCommonFriends(int id, int otherId);
}
