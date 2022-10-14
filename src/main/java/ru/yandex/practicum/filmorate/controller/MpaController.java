package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
    public Mpa getById(@PathVariable int id) {
        Optional<Mpa> mpa = mpaStorage.getById(id);
        if (mpa.isEmpty()) {
            throw new NotFoundException("По указанному id не найден MPA");
        } else {
            log.info("запрошен MPA id{}", id);
            return mpa.get();
        }
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("запрошены все MPA");
        return mpaStorage.findAll();
    }
}