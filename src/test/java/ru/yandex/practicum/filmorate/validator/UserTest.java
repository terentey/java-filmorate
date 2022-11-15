package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidatorTest;

import java.time.LocalDate;



public class UserTest {
    private final static String LOGIN = "dolore";
    private final static String NAME = "Nick Name";
    private final static String EMAIL = "mail@mail.ru";
    private final static LocalDate BIRTHDAY = LocalDate.of(1946,8,20);

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Создание корректного юзера")
    public void createUser() {
        setUpUser(LOGIN, NAME, EMAIL, BIRTHDAY);
        Assertions.assertTrue(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с некорректным логином")
    public void createUserWithFailLogin() {
        setUpUser("dol ore", NAME, EMAIL, BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }
    @Test
    @DisplayName("Создание юзера с пустым логином")
    public void createUserWithEmptyLogin() {
        setUpUser(null, NAME, EMAIL, BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с некорректной почтой")
    public void createUserWithFailEmail() {
        setUpUser(LOGIN, NAME, "mail.ru", BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с пустой почтой")
    public void createUserWithEmptyEmail() {
        setUpUser(LOGIN, NAME, null, BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с некорректным ДР")
    public void createUserWithFailBirthday() {
        setUpUser(LOGIN, NAME, EMAIL, LocalDate.of(2846,8,20));
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с пустым ДР")
    public void createUserWithEmptyBirthday() {
        setUpUser(LOGIN, NAME, EMAIL, null);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с пустым именем")
    public void createUserWithEmptyName() {
        setUpUser(LOGIN, null, EMAIL, BIRTHDAY);
        Assertions.assertTrue(ValidatorTest.getViolations(user).isEmpty());
    }

    private void setUpUser(String login, String name, String email, LocalDate birthday) {
        user.setLogin(login);
        user.setName(name);
        user.setEmail(email);
        user.setBirthday(birthday);
    }


}
