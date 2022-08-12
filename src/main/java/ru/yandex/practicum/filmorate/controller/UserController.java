package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
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
        for(User existingUser : users.values()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                log.warn("Указанный адрес электронной почты уже используется.");
                throw new ConflictException("Указанный адрес электронной почты уже используется.");
            }
        }
        if (users.containsKey(user.getId())) {
            throw new ConflictException("Указанный id уже используется");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping //обновление пользователя
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.debug("Обновлена карточка пользователя: {}", user);
            return user;
        } else {
            log.warn("Пользователя с указанным id не существует: {}", user.getId());
            throw new NotFoundException("Пользователя с указанным id не существует.");
        }
    }

}
