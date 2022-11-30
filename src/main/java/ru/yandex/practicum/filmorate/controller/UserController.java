package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.debug("Валидация пройдена.");
        return service.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.debug("Валидация пройдена.");
        return service.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        service.addFriend(id, friendId);
    }

    @GetMapping
    public Collection<User> find() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable int id) {
        return service.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return service.findCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        service.deleteFriend(id, friendId);
    }
}
