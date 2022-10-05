package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreDbStorage genreStorage;

    public GenreController(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping("/{id}")
    public Optional<Genre> getById(@PathVariable int id) {
        log.info("запрошен жанр id{}", id);
        return genreStorage.getById(id);
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("запрошены все жанры");
        return genreStorage.findAll();
    }
}