package ru.yandex.practicum.filmorate.storage;

import org.springframework.data.relational.core.sql.In;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public interface FilmStorage {
    Optional<Film> create(Film film);

    Optional<Film> update(Film film);

    List<Film> findAll();

    void delete(int id);

    void deleteAll();

    Optional<Film> getById(Long id);

    void addLike(int idUser, Long idFilm);

    void removeLike(int idUser, Long idFilm);

    List<Optional<Film>> getPopular(Integer limit);

    Mpa getMpa(int idMpa);

    TreeSet<Genre> getGenres(Film film);
}
