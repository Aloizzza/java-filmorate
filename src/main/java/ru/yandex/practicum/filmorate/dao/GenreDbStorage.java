package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Genre> getById(Integer id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("unknown genre");
        }
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE ID_GENRE=?", id);
        if (genreRow.next()) {
            Genre genre = new Genre(
                    genreRow.getInt("id_genre"),
                    genreRow.getString("name")
            );
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    public List<Genre> findAll() {
        List<Genre> list = new ArrayList<>();
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES");
        while (genreRow.next()) {
            Genre genre = new Genre(
                    genreRow.getInt("id_genre"),
                    genreRow.getString("name")
            );
            list.add(genre);
        }
        return list;
    }
}