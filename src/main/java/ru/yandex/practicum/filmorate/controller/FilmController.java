package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping //получение всех фильмов
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping //добавление фильма
    public Film create(@RequestBody Film film) {
        validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping //обновление фильма
    public Film update(@RequestBody Film film) {
        validateFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлена карточка фильма: {}", film);
            return film;
        } else {
            log.warn("Фильм с указанным id не существует: {}", film.getId());
            throw new InvalidFilmException("Фильм с указанным id не существует.");
        }
    }

    private void validateFilm(Film film) {
        if (film.getName().isBlank() || film.getName().isEmpty() || film.getName() == null) {
            log.warn("Название фильма не может быть пустым.");
            throw new InvalidFilmException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов.");
            throw new InvalidFilmException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза фильма не может быть раньше изобретения кино.");
            throw new InvalidFilmException("Дата релиза фильма не может быть раньше изобретения кино.");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной.");
            throw new InvalidFilmException("Продолжительность фильма должна быть положительной.");
        }
    }

}
