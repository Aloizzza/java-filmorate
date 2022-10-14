package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        Film newFlm = filmService.create(film);
        log.info("добавлен фильм: {}", film);
        return newFlm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        Film updatedFilm = filmService.update(film);
        log.info("обновлен фильм: {}", film);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        log.info("пользователю id{} понравился фильм id{}", userId, id);
    }

    @GetMapping
    public List<Film> findAll() {
        List<Film> films = filmService.findAll();
        log.info("запрошены все фильмы, общее количество: {}", films.size());
        return films;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        Film film = filmService.getById(id);
        log.info("запрошен фильм id{}", id);
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("запрошены популярные фильмы в количестве {}", count);
        return filmService.getPopular(count);
    }

    @DeleteMapping
    public void deleteAll() {
        filmService.deleteAll();
        log.info("удалены все фильмы");
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id) {
        filmService.delete(id);
        log.info("удален фильм id{}", id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        log.info("пользователею id{} разонравился фильм id{}", userId, id);
    }
}