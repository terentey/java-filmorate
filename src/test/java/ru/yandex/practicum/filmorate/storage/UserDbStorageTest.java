package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.util.TestUser;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDbStorageTest {
    private final UserDbStorage storage;
    private static User user0;
    private static User user1;
    private static User user2;

    @BeforeAll
    public static void setUp() {
        user0 = TestUser.newTestUser(0);
        user1 = TestUser.newTestUser(1);
        user2 = TestUser.newTestUser(2);
    }

    @Order(1)
    @Test
    @DisplayName("Создание юзера")
    public void createTest() {
        user0.setId(1);
        user1.setId(2);
        user2.setId(3);
        Assertions.assertEquals(user0, storage.create(user0));
        Assertions.assertEquals(user1, storage.create(user1));
        Assertions.assertEquals(user2, storage.create(user2));
    }

    @Order(2)
    @Test
    @DisplayName("Обновление юзера")
    public void updateTest() {
        user0.setName("updateName");
        Assertions.assertEquals(user0, storage.update(user0));
    }

    @Order(3)
    @Test
    @DisplayName("Найти всех юзеров")
    public void findAllTest() {
        List<User> ls = storage.findAll();
        Assertions.assertTrue(ls.contains(user0));
        Assertions.assertTrue(ls.contains(user1));
        Assertions.assertTrue(ls.contains(user2));
        Assertions.assertEquals(3, ls.size());
    }

    @Order(4)
    @Test
    @DisplayName("Найти по id")
    public void findByIdTest() {
        Assertions.assertEquals(user1, storage.findById(2).orElseThrow(IncorrectIdException::new));
    }

    @Order(5)
    @Test
    @DisplayName("Несуществующий id")
    public void findByIncorrectIdTest() {
        Assertions.assertThrows(IncorrectIdException.class,
                () -> storage.findById(99).orElseThrow(IncorrectIdException::new));
    }

    @Order(6)
    @Test
    @DisplayName("Добавление друга с несуществующим id")
    public void addIncorrectFriend() {
        Assertions.assertThrows(IncorrectIdException.class, () -> storage.addFriend(1, 99));
    }

    @Order(7)
    @Test
    @DisplayName("Найти всех друзей юзера с id=1")
    public void findAllFriendsTest() {
        storage.addFriend(1, 2);
        storage.addFriend(1, 3);
        List<User> ls = storage.findFriends(1);
        Assertions.assertTrue(ls.contains(user1));
        Assertions.assertTrue(ls.contains(user2));
        Assertions.assertEquals(2, ls.size());
    }

    @Order(8)
    @Test
    @DisplayName("Найти общих друзей юзеров с id=1 и id=3")
    public void findCommonFriendsTest() {
        storage.addFriend(3,2);
        Assertions.assertTrue(storage.findCommonFriends(1,3).contains(user1));
    }

    @Order(9)
    @Test
    @DisplayName("Удалить друга с id=1 у юзера с id=2")
    public void deleteFriendTest(){
        storage.addFriend(2, 1);
        storage.deleteFriend(2, 1);
        Assertions.assertFalse(storage.findFriends(2).contains(user0));
    }
}
