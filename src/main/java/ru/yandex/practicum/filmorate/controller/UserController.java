package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    private Integer id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.debug("Валидация пройдена.");
        checkName(user);
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.debug("Валидация пройдена.");
        checkName(user);
        if(!users.containsKey(user.getId())) throw new IncorrectIdException();
        users.put(id, user);
        return user;
    }
    @GetMapping
    public Collection<User> find() {
        return users.values();
    }

    private void checkName(User user) {
        String name = user.getName();
        if(name == null || name.isBlank()) user.setName(user.getLogin());
    }
}
