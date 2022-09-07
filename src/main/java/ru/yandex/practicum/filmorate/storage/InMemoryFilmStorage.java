package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private final Set<Film> ratedFilms = new TreeSet<>((film1, film2) -> {
        if ((film1.getLikes(film1.getId()) != 0) && (film2.getLikes(film2.getId()) != 0)) {
            return film1.getLikes(film1.getId()).compareTo(film2.getLikes(film2.getId()));
        } else if (film1.getLikes(film1.getId()) == 0) {
            return 1;
        } else if (0 == film2.getLikes(film2.getId())) {
            return -1;
        } else {
            return 0;
        }
    });
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}", film);

        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с указанным id не существует: " + film.getId());
        }
        films.put(film.getId(), film);
        log.debug("Обновлена карточка фильма: {}", film);

        return film;
    }

    public Film getById(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с указанным id не существует: " + id);
        }
        return films.get(id);
    }

    public Set<Film> getPopular() {
        ratedFilms.clear();
        for (int i = 1; i < id + 1; i++) {
            if (films.containsKey(i)) {
                ratedFilms.add(films.get(id));
            }
        }
        return ratedFilms;
    }

}
