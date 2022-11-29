package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidatorTest;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.util.TestUser.setUpUser;

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
        setUpUser(user, LOGIN, NAME, EMAIL, BIRTHDAY);
        Assertions.assertTrue(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с некорректным логином")
    public void createUserWithFailLogin() {
        setUpUser(user, "dol ore", NAME, EMAIL, BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }
    @Test
    @DisplayName("Создание юзера с пустым логином")
    public void createUserWithEmptyLogin() {
        setUpUser(user, null, NAME, EMAIL, BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с некорректной почтой")
    public void createUserWithFailEmail() {
        setUpUser(user, LOGIN, NAME, "mail.ru", BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с пустой почтой")
    public void createUserWithEmptyEmail() {
        setUpUser(user, LOGIN, NAME, null, BIRTHDAY);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с некорректным ДР")
    public void createUserWithFailBirthday() {
        setUpUser(user, LOGIN, NAME, EMAIL, LocalDate.of(2846,8,20));
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с пустым ДР")
    public void createUserWithEmptyBirthday() {
        setUpUser(user, LOGIN, NAME, EMAIL, null);
        Assertions.assertFalse(ValidatorTest.getViolations(user).isEmpty());
    }

    @Test
    @DisplayName("Создание юзера с пустым именем")
    public void createUserWithEmptyName() {
        setUpUser(user, LOGIN, null, EMAIL, BIRTHDAY);
        Assertions.assertTrue(ValidatorTest.getViolations(user).isEmpty());
    }
}
