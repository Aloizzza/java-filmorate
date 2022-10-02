package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private final int idUser;
    private final int idFriend;
}