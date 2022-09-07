package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConflictException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        if (users.containsKey(user.getId())) {
            throw new ConflictException("Указанный id уже используется");
        }
        defineName(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Добавлен новый пользователь: {}", user);

        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Не существует пользователя с указанным id: " + user.getId());
        }

        defineName(user);

        users.put(user.getId(), user);
        log.debug("Обновлена карточка пользователя: {}", user);

        return user;
    }

    public User getById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Не существует пользователя с указанным id: " + id);
        }
        return users.get(id);
    }

    private void defineName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
