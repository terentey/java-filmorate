package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User createUser(User user) {
        return storage.create(user);
    }

    public User updateUser(User user) {
        return storage.update(user);
    }

    public Collection<User> findAllUser() {
        return storage.findAll();
    }

    public User findUserById(int id) {
        return storage.findById(id);
    }

    public void addFriend(int id, int friendId) {
        User user = storage.findById(id);
        storage.findById(friendId).getFriends().add(id);
        user.getFriends().add(friendId);
    }

    public List<User> findFriends(int id) {
        return storage.findFriends(id);
    }

    public List<User> findCommonFriends(int id, int otherId) {
        return storage.findCommonFriends(id, otherId);
    }

    public void deleteFriend(int id, int friendId) {
        storage.containsUser(friendId);
        storage.findById(id).getFriends().remove(friendId);
    }
}
