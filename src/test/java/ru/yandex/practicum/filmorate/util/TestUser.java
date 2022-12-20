package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class TestUser {
    public static void setUpUser(User user, String login, String name, String email, LocalDate birthday) {
        user.setLogin(login);
        user.setName(name);
        user.setEmail(email);
        user.setBirthday(birthday);
    }

    public static User newTestUser(int num) {
        User user = new User();
        setUpUser(user,
                "login_" + num,
                "name_" + num,
                String.format("test_%d@mail.com",num),
                LocalDate.of(2000,1,1).plusDays(num));
        return user;
    }
}
