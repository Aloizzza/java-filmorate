package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Mpa> getById(Integer id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("unknown MPA");
        }
        SqlRowSet MpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM MPA_RATING WHERE ID_RATE=?", id);
        if (MpaRow.next()) {
            Mpa mpa = new Mpa(
                    MpaRow.getInt("id_rate"),
                    MpaRow.getString("name")
            );
            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

    public List<Optional<Mpa>> findAll() {
        List<Optional<Mpa>> list = new ArrayList<>();
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT * FROM MPA_RATING");
        while (mpaRow.next()) {
            Mpa mpa = new Mpa(
                    mpaRow.getInt("id_rate"),
                    mpaRow.getString("name")
            );
            list.add(Optional.of(mpa));
        }
        return list;
    }
}