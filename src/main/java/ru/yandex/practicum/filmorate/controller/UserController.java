package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidFilmException;
import ru.yandex.practicum.filmorate.exception.InvalidUserException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping //получение списка всех пользователей
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping //создание пользователя
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        for(User existingUser : users.values()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                log.warn("Указанный адрес электронной почты уже используется.");
                throw new InvalidEmailException("Указанный адрес электронной почты уже используется.");
            }
        }
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Указанный id уже используется");
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping //обновление пользователя
    public User update(@Valid @RequestBody User user) {
        validateUser(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Обновлена карточка пользователя: {}", user);
            return user;
        } else {
            log.warn("Пользователя с указанным id не существует: {}", user.getId());
            throw new InvalidUserException("Пользователя с указанным id не существует.");
        }
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @.");
            throw new InvalidEmailException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы.");
            throw new InvalidUserException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName()== null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new InvalidUserException("Дата рождения не может быть в будущем.");
        }
    }

}
