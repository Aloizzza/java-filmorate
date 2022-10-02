package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaDbStorage mpaStorage;

    public MpaController(MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping("/{id}")
    public Optional<Mpa> getById(@PathVariable int id) {
        log.info("запрошен MPA id{}", id);
        return mpaStorage.getById(id);
    }

    @GetMapping
    public List<Optional<Mpa>> getAll() {
        log.info("запрошены все mpa");
        return mpaStorage.findAll();
    }
}