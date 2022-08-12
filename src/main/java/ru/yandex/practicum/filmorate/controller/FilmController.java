package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
    public Film create(@Valid @RequestBody Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping //обновление фильма
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлена карточка фильма: {}", film);
            return film;
        } else {
            log.warn("Фильм с указанным id не существует: {}", film.getId());
            throw new NotFoundException("Фильм с указанным id не существует.");
        }
    }

}
