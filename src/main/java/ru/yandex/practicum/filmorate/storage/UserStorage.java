package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> create(User user);

    Optional<User> update(User user);

    List<User> findAll();

    void delete(int id);

    void deleteAll();

    Optional<User> getById(Integer id);

    void addFriend(int id, int idFriend);

    void deleteFriend(int id, int idFriend);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int id2);
}