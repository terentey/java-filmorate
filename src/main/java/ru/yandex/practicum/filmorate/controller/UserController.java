package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Integer id = 0;
    Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.debug("Валидация пройдена.");
        if(user.getName() == null) user.setName(user.getLogin());
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.debug("Валидация пройдена.");
        if(!users.containsKey(user.getId())) throw new IncorrectIdException();
        users.put(id, user);
        return user;
    }
    @GetMapping
    public Collection<User> find() {
        return users.values();
    }
}
