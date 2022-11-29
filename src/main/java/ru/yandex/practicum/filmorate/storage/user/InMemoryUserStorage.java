package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        checkName(user);
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        checkName(user);
        containsUser(user.getId());
        users.put(id, user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(int id) {
        containsUser(id);
        return users.get(id);
    }

    @Override
    public List<User> findFriends(int id) {
        containsUser(id);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : users.get(id).getFriends()) {
            friends.add(users.get(friendId));
        }
        return friends;
    }

    @Override
    public List<User> findCommonFriends(int id, int otherId) {
        containsUser(id);
        containsUser(otherId);
        Set<Integer> friendsOtherPerson = users.get(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        for(Integer friendId : users.get(id).getFriends()) {
            if(friendsOtherPerson.contains(friendId)) commonFriends.add(users.get(friendId));
        }
        return commonFriends;
    }

    @Override
    public void containsUser(int id) {
        if(!users.containsKey(id)) throw new IncorrectIdException();
    }

    private void checkName(User user) {
        String name = user.getName();
        if(name == null || name.isBlank()) user.setName(user.getLogin());
    }
}
