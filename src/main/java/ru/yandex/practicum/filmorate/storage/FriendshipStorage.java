package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendshipStorage {
    void addFriend(int userId, int friendId);

    void removeFriend(User user, User friend);
}