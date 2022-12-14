package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        getById(film.getId());
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void delete(int id) {
        filmStorage.delete(id);
    }

    public void deleteAll() {
        filmStorage.deleteAll();
    }

    public Film getById(Long id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("По указанному id не найден фильм"));
    }

    public void addLike(Long idFilm, int idUser) {
        filmStorage.getById(idFilm);
        userStorage.getById(idUser);
        filmStorage.addLike(idUser, idFilm);
    }

    public void removeLike(Long idFilm, int idUser) {
        if (filmStorage.getById(idFilm).isEmpty() || userStorage.getById(idUser).isEmpty()) {
            throw new NotFoundException("По указанным id не найден фильм или пользователь");
        } else {
            filmStorage.removeLike(idUser, idFilm);
        }
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }
}