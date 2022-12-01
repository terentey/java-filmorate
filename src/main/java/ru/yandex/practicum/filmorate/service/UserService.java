package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User create(User user) {
        checkName(user);
        return storage.create(user);
    }

    public User update(User user) {
        checkName(user);
        return storage.update(user);
    }

    public List<User> findAll() {
        return storage.findAll();
    }

    public User findById(int id) {
        return storage.findById(id).orElseThrow(IncorrectIdException::new);
    }

    public void addFriend(int id, int friendId) {
        User user = findById(id);
        findById(friendId).getFriends().add(id);
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
        findById(id).getFriends().remove(friendId);
    }

    private void checkName(User user) {
        String name = user.getName();
        if(name == null || name.isBlank()) user.setName(user.getLogin());
    }
}
