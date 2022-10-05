package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserDbStorage userStorage;

    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> getById(int id) {
        return userStorage.getById(id);
    }

    public Optional<User> create(User user) {
        return userStorage.create(user);
    }

    public Optional<User> update(User user) {
        if (user.getId() <= 0 || user.getId() == null) {
            throw new NotFoundException("id должен быть > 0");
        }
        if (getById(user.getId()).isEmpty()) {
            throw new NotFoundException("пользователь для update не найден");
        }
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
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null
                && !(id1 <= 0) && !(id2 <= 0)) {
            userStorage.addFriend(id1, id2);
        } else {
            throw new NotFoundException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }

    }

    public void removeFriend(int id1, int id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null
                && !(id1 <= 0) && !(id2 <= 0)) {
            userStorage.deleteFriend(id1, id2);
        } else {
            throw new NotFoundException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }
    }

    public List<User> getFriends(int id) {
        List<User> list = userStorage.getFriends(id);
        return list;
    }

    public List<User> getCommonFriends(int id1, int id2) {
        List<User> commonFriends = userStorage.getCommonFriends(id1, id2);
        return commonFriends;
    }
}