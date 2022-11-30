package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.TestUser.newTestUser;

public class UserServiceTest {
    private static User user1;
    private static User user2;
    private static User user3;
    private static User user4;
    private static User user5;
    private static User user6;
    private static UserService service = new UserService(new InMemoryUserStorage());

    @BeforeAll
    public static void setUp() {
        user1 = newTestUser(1);
        user2 = newTestUser(2);
        user3 = newTestUser(3);
        user4 = newTestUser(4);
        user5 = newTestUser(5);
        user6 = newTestUser(6);
        service.create(user1);
        service.create(user2);
        service.create(user3);
        service.create(user4);
        service.create(user5);
        service.create(user6);
    }

    @Test
    @DisplayName("Обновление юзера")
    public void updateUserTest() {
        int num = 7;
        User newUser = newTestUser(num);
        service.create(newUser);
        User updateUser = newTestUser(8);
        updateUser.setId(num);
        service.update(updateUser);
        Assertions.assertEquals(updateUser, service.findById(num));
    }

    @Test
    @DisplayName("Добавление друзей")
    public void addFriendTest() {
        service.addFriend(1,2);
        service.addFriend(1,3);
        service.addFriend(1,4);
        service.addFriend(1,5);
        Assertions.assertEquals(service.findFriends(1), List.of(user2, user3, user4, user5));
    }

    @Test
    @DisplayName("Нахождение общих друзей")
    public void findCommonFriendsTest() {
        service.addFriend(6, 3);
        service.addFriend(6, 4);
        Assertions.assertEquals(service.findCommonFriends(1,6), List.of(user3, user4));
    }

    @Test
    @DisplayName("Удаление из друзей")
    public void deleteFriendTest() {
        service.deleteFriend(1,5);
        Assertions.assertFalse(service.findFriends(1).contains(user5));
    }
}
