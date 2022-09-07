package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exception.ConflictException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class User {
    private int id;
    @Email(message = "Ошибка в формате почты.")
    private String email;
    @NotBlank(message = "Поле 'Логин' не может быть пустым.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не модет быть в будущем.")
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        if (friends.contains(id)) {
            throw new ConflictException("У пользователя уже есть друг с указанным id: " + id);
        }
        friends.add(id);
    }

    public void removeFriend(int id) {
        if (!friends.contains(id)) {
            throw new NotFoundException("У пользователя нет друга с указанным id: " + id);
        }
        friends.remove(id);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public void setFriends(Set<Integer> friends) {
        this.friends = friends;
    }
}
