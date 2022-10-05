package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
        filmService.create(film);
        log.info("добавлен фильм: {}", film);
        return filmService.getById(film.getId()).get();
    }

    @PutMapping
    public Optional<Film> update(@Valid @RequestBody Film film) {
        log.info("обновлен фильм: {}", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        log.info("пользователю id{} понравился фильм id{}", userId, id);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("запрошены все фильмы, общее количество: {}", filmService.findAll().size());
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Film> getById(@PathVariable Long id) {
        log.info("запрошен фильм id{}", id);
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<Optional<Film>> getPopular(@RequestParam(defaultValue = "0") Integer count) {
        log.info("запрошены популярные фильмы в количестве {}", ((count == 0) ? 10 : count));
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