package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        userService.create(user);
        log.info("добавлен новый пользователь: {}", user);
        return userService.getById(user.getId()).get();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        userService.update(user);
        log.info("обновлен пользователь: {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
        log.info("к пользователю id{} добавлен в друзья id{}", id, friendId);
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable int id) {
        log.info("запрошен пользователь id{}", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<User> findAll() {
        log.info("запрошены все пользователи, общее количество: {}", userService.findAll().size());
        return userService.findAll();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.debug("Запрошен список друзей пользователя id{}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Запрошен список общих друзей id1{} и id2{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @DeleteMapping
    public void deleteAll() {
        userService.deleteAll();
        log.info("удалены все пользователи");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        userService.deleteUser(id);
        log.info("удален пользователь id{}", id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
        log.info("из друзей пользователя id{} удален id{}", id, friendId);
    }
}