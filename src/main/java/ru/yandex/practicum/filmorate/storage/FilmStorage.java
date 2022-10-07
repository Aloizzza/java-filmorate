package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

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
}
