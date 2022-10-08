package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
    public Genre getById(@PathVariable int id) {
        Optional<Genre> genre = genreStorage.getById(id);
        if (genre.isPresent()) {
            log.info("запрошен жанр id{}", id);
            return genre.get();
        } else {
            throw new NotFoundException("По указанному id не найден жанр");
        }
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("запрошены все жанры");
        return genreStorage.findAll();
    }
}