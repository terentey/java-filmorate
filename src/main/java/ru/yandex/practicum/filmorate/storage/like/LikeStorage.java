package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    void create(int id, int userId);
    void delete(int id, int userId);
}
