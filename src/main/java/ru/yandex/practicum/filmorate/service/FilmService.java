package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    protected final FilmStorage filmStorage;
    protected final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void addLike(int filmId, int userId) {
        if (!(filmStorage.getFilmById(filmId) == null) && !(userService.getUserById(userId) == null)) {
            filmStorage.getFilmById(filmId).addLike(userId);
        }
    }

    public void removeLike(Integer userId, Integer filmId) {
        if (!(filmStorage.getFilmById(filmId) == null) && !(userService.getUserById(userId) == null)) {
            filmStorage.getFilmById(filmId).removeLike(userId);
        }
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular()
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

}
