package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    List<User> findAll(int id);
    List<User> findCommon(int id, int otherId);
    void create(int id, int friendId);
    void delete(int id, int friendId);
}
