package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final MpaDbStorage mpaStorage;

    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, MpaDbStorage mpaDbStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaDbStorage;
    }

    public Optional<Film> create(Film film) {
        return filmStorage.create(film);
    }

    public Optional<Film> update(Film film) {
        if (film.getId() <= 0 || film.getId() == null) {
            throw new NotFoundException("id должен быть > 0");
        }
        if (getById(film.getId()).isEmpty()) {
            throw new NotFoundException("фильм для update не найден");
        }
        if (film.getMpa().getId() == null) {
            throw new BadRequestException("фильм должен иметь рейтинг MPA");
        }
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

    public Optional<Film> getById(Long id) {
        return filmStorage.getById(id);
    }

    public void addLike(Long idFilm, int idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null
                && idFilm > 0 && idUser > 0) {
            filmStorage.addLike(idUser, idFilm);
        } else {
            throw new NotFoundException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    public void removeLike(Long idFilm, int idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null) {
            filmStorage.removeLike(idUser, idFilm);
        } else {
            throw new NotFoundException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    public List<Optional<Film>> getPopular(int countRate) {
        Integer count = countRate;
        if (count == 0 || count == null) {
            count = 10;
        }
        List<Optional<Film>> listOrder = filmStorage.getPopular(count);
        return listOrder;
    }
}