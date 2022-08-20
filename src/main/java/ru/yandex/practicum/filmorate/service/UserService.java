package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    protected final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        if (!(userStorage.getUserById(userId)==null) && !(userStorage.getUserById(friendId)==null)) {
            getUserById(userId).addFriend(friendId);
            getUserById(friendId).addFriend(userId);
        }
    }

    public void removeFriend(int userId, int friendId) {
        getUserById(userId).removeFriend(friendId);
        getUserById(friendId).removeFriend(userId);
    }

    public List<User> getUserFriends(int id) {
        List<User> userFriends = new ArrayList<>();
        for (int friendId : getUserById(id).getFriends()) {
            userFriends.add(getUserById(friendId));
        }
        return userFriends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> userFriends = getUserById(userId).getFriends();
        Set<Integer> otherFriends = getUserById(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        for (int userFriend : userFriends) {
            if (otherFriends.contains(userFriend)) {
                commonFriends.add(userStorage.getUserById(userFriend));
            }
        }
        return commonFriends;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

}
