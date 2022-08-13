package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConflictException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
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

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Не существует пользователя с указанным id: " + user.getId());
        }

        defineUserName(user);

        users.put(user.getId(), user);
        log.debug("Обновлена карточка пользователя: {}", user);

        return user;
    }

    private void defineUserName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
