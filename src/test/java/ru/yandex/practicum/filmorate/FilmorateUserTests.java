package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateUserTests {

    @Autowired
    private UserController userController;

    @BeforeEach
    public void clearDB() {
        userController.deleteAll();
    }


    @Test
    public void userControllerValidEntityTest() {
        User user = new User(1, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));

        userController.create(user);
        Assertions.assertEquals(userController.getCountUsers(), 1);
    }

    @Test
    void userControllerInvalidEmailTest() {

        User user1 = new User(1, "user1.example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));


        assertThrows(RuntimeException.class, () -> userController.create(user1));

    }

    @Test
    public void userControllerInvalidLoginTest() {
        User user = new User(2, "user2@example", "User2"
                , "", LocalDate.of(2000, 1, 1));


        assertThrows(RuntimeException.class, () -> userController.create(user));
        user.setLogin(null);
        assertThrows(ConstraintViolationException.class, () -> userController.create(user));

    }

    @Test
    public void userControllerInvalidDateBirthTest() {
        User user = new User(2, "user2@example", "User2"
                , "user2", LocalDate.of(2030, 1, 1));
        user.setId(2);
        user.setBirthday(LocalDate.of(2030, 1, 1));
        user.setName("User2");
        user.setLogin("user2");
        user.setEmail("user2@example");

        assertThrows(RuntimeException.class, () -> userController.create(user));
    }

    @Test
    public void userControllerUpdateTest() {
        User user = new User(1, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));

        User userUpd = new User(1, "userNew@example", "UserNew"
                , "userNew", LocalDate.of(2002, 1, 1));

        userController.create(user);
        userController.update(userUpd);
        Assertions.assertEquals(userController.getCountUsers(), 1);
    }

    @Test
    public void userControllerGetTest() {
        User user = new User(1, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));

        User user2 = new User(2, "userNew@example", "UserNew"
                , "userNew", LocalDate.of(2002, 1, 1));

        userController.create(user);
        userController.create(user2);
        Assertions.assertEquals(userController.findAll().size(), 2);
    }
}