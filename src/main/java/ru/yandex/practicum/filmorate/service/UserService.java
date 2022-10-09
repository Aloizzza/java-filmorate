package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserService {

    private final UserDbStorage userStorage;

    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getById(int id) {
        return userStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("По указанному id не найден пользователь"));
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        getById(user.getId());
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void deleteUser(int id) {
        userStorage.delete(id);
    }

    public void deleteAll() {
        userStorage.deleteAll();
    }

    public void addFriend(int id1, int id2) {
        if (userStorage.getById(id1).isEmpty() || userStorage.getById(id2).isEmpty()) {
            throw new NotFoundException("По указанным id не найден один из пользователей");
        } else {
            userStorage.addFriend(id1, id2);
        }
    }

    public void removeFriend(int id1, int id2) {
        if (userStorage.getById(id1).isEmpty() || userStorage.getById(id2).isEmpty()) {
            throw new NotFoundException("По указанным id не найден один из пользователей");
        } else {
            userStorage.deleteFriend(id1, id2);
        }
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(int id1, int id2) {
        return userStorage.getCommonFriends(id1, id2);
    }
}