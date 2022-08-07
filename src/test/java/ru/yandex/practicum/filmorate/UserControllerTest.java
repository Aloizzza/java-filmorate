package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidFilmException;
import ru.yandex.practicum.filmorate.exception.InvalidUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();
    User testUser = new User();
    User testUser2 = new User();

    @Test
    public void shouldBeEmpty() {
        assertTrue(userController.findAll().isEmpty());
    }

    @Test
    void shouldAddUser() throws InvalidFilmException {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        assertEquals(1, userController.findAll().size());
    }

    @Test
    void shouldNotAddUserWithEmptyEmail() {
        testUser.setEmail("");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));

        InvalidEmailException e = assertThrows(
                InvalidEmailException.class,
                () -> userController.create(testUser)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
    }

    @Test
    void shouldNotAddUserWithSameEmail() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        testUser2.setEmail("aloizzza@yandex.ru");
        testUser2.setLogin("Volchok");
        testUser2.setName("Максим");
        testUser2.setBirthday(LocalDate.of(1994, 11, 23));

        InvalidEmailException e = assertThrows(
                InvalidEmailException.class,
                () -> userController.create(testUser2)
        );
        assertEquals("Указанный адрес электронной почты уже используется.", e.getMessage());
        assertEquals(1, userController.findAll().size());
    }

    @Test
    void shouldNotAddUserWithWrongFormatOfEmail() {
        testUser.setEmail("aloizzza.yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));

        InvalidEmailException e = assertThrows(
                InvalidEmailException.class,
                () -> userController.create(testUser)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
    }

    @Test
    void shouldNotAddUserWithEmptyLogin() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));

        InvalidUserException e = assertThrows(
                InvalidUserException.class,
                () -> userController.create(testUser)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы.", e.getMessage());
    }

    @Test
    void shouldNotAddUserWithGapInLogin() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloiz zza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));

        InvalidUserException e = assertThrows(
                InvalidUserException.class,
                () -> userController.create(testUser)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы.", e.getMessage());
    }

    @Test
    void shouldAddUserWithEmptyName() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        assertEquals(1, userController.findAll().size());
    }

    @Test
    void shouldNotAddUserBornInFuture() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(2050, 3, 20));

        InvalidUserException e = assertThrows(
                InvalidUserException.class,
                () -> userController.create(testUser)
        );
        assertEquals("Дата рождения не может быть в будущем.", e.getMessage());
    }

    @Test
    void shouldUpdateUser() throws InvalidFilmException {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        testUser.setId(1);
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Максим");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.update(testUser);

        assertEquals(1, userController.findAll().size());
    }

    @Test
    void shouldNotUpdateUserWithEmptyEmail() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        testUser.setEmail("");

        InvalidEmailException e = assertThrows(
                InvalidEmailException.class,
                () -> userController.update(testUser)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithWrongFormatOfEmail() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        testUser.setEmail("aloizzza.yandex.ru");

        InvalidEmailException e = assertThrows(
                InvalidEmailException.class,
                () -> userController.update(testUser)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithEmptyLogin() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        testUser.setLogin("");

        InvalidUserException e = assertThrows(
                InvalidUserException.class,
                () -> userController.update(testUser)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы.", e.getMessage());
    }

    @Test
    void shouldNotUpdateUserWithGapInLogin() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        testUser.setLogin("Aloiz zza");

        InvalidUserException e = assertThrows(
                InvalidUserException.class,
                () -> userController.update(testUser)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы.", e.getMessage());
    }

    @Test
    void shouldNotUpdateUserBornInFuture() {
        testUser.setEmail("aloizzza@yandex.ru");
        testUser.setLogin("Aloizzza");
        testUser.setName("Екатерина");
        testUser.setBirthday(LocalDate.of(1986, 3, 20));
        userController.create(testUser);

        testUser.setBirthday(LocalDate.of(2050, 3, 20));

        InvalidUserException e = assertThrows(
                InvalidUserException.class,
                () -> userController.update(testUser)
        );
        assertEquals("Дата рождения не может быть в будущем.", e.getMessage());
    }
}