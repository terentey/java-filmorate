package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        containsUser(user.getId());
        users.put(id, user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findFriends(int id) {
        containsUser(id);
        Set<Integer> friends = users.get(id).getFriends();
        return users.values()
                .stream()
                .filter(u -> friends.contains(u.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        containsUser(id);
        containsUser(otherId);
        Set<Integer> friends = users.get(id).getFriends();
        Set<Integer> friendsOtherPerson = users.get(otherId).getFriends();
        return users.values()
                .stream()
                .filter(u -> friends.contains(u.getId()) && friendsOtherPerson.contains(u.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void containsUser(int id) {
        if(!users.containsKey(id)) throw new IncorrectIdException();
    }
}
