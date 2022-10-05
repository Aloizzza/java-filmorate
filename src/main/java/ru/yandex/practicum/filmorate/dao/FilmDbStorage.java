package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private int filmId = 0;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private int getFilmId() {
        return ++filmId;
    }


    @Override
    public Optional<Film> create(Film film) {

        int id = getFilmId();

        jdbcTemplate.update("INSERT INTO films(id_film, name, description, release_date, duration, rate, mpa) " +
                        "values(?,?,?,?,?,?,?)", id, film.getName(), film.getDescription(), film.getReleaseDate()
                , film.getDuration(), film.getRate(), film.getMpa().getId());
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(
                "SELECT f.id_film id_film,m.name name " +
                        "FROM films f " +
                        "JOIN mpa_rating m ON f.mpa=m.id_rate " +
                        "WHERE f.id_film=?", id);
        if (mpaRow.next()) {
            film.getMpa().setName(mpaRow.getString("name"));
            film.setId(mpaRow.getLong("id_film"));
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("MERGE INTO film_genre_link (id_film, id_genre) KEY (id_film, id_genre) " +
                        "VALUES (?, ?)", film.getId(), genre.getId());
            }
        }

        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        jdbcTemplate.update("DELETE FROM film_genre_link WHERE id_film=?", film.getId());
        jdbcTemplate.update("MERGE INTO films (id_film, name, description, release_date, duration, rate, mpa) " +
                        "KEY (id_film) VALUES (?,?, ?, ?, ?, ?, ?)", film.getId(), film.getName()
                , film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate()
                , film.getMpa().getId());
        //film.setMpa(getMpa(film.getMpa().getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("MERGE INTO film_genre_link (id_film, id_genre) KEY (id_film, id_genre) " +
                        "VALUES (?, ?)", film.getId(), genre.getId());
            }
            //film.setGenres(getGenres(film));
        }
        //return Optional.of(film);
        return getById(film.getId());
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        Mpa mpa;
        SqlRowSet filmRows =
                jdbcTemplate.queryForRowSet("SELECT f.id_film,f.name FNAME,f.description,f.release_date" +
                        ",f.duration,f.rate,f.mpa,m.id_rate,m.name MNAME " +
                        "FROM films f " +
                        "JOIN mpa_rating m ON m.id_rate=f.mpa");
        while (filmRows.next()) {
            Long idFilm = filmRows.getLong("id_film");
            mpa = new Mpa(filmRows.getInt("id_rate"), filmRows.getString("MNAME"));

            Film film = new Film(
                    idFilm,
                    filmRows.getString("FNAME"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate")
            );
            film.setMpa(mpa);
            film.setGenres(getGenres(film));
            films.add(film);
        }
        return films;
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM films WHERE id_film=?; DELETE FROM film_genre_link WHERE id_film=?"
                , id, id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM film_genre_link; DELETE FROM rating; DELETE FROM films");
        filmId = 0;
    }

    @Override
    public Optional<Film> getById(Long id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("фильм не найден");
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id_film=?", id);
        if (filmRows.next()) {

            Film film = new Film(
                    filmRows.getLong("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate")
            );
            film.setMpa(getMpa(filmRows.getInt("mpa")));
            film.setGenres(getGenres(film));
            return Optional.of(film);
        } else {
            throw new NotFoundException("По указанному id не найден фильм");
        }

    }

    @Override
    public void addLike(int idUser, Long idFilm) {
        jdbcTemplate.update("INSERT INTO rating(id_user, id_film) VALUES(?,?)", idUser, idFilm);
    }

    @Override
    public void removeLike(int idUser, Long idFilm) {
        jdbcTemplate.update("DELETE FROM rating WHERE id_user=? AND id_film=?", idUser, idFilm);
    }

    @Override
    public List<Optional<Film>> getPopular(Integer limit) {
        List<Optional<Film>> listRate = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.id_film, f.name, f.description, f.duration, " +
                        "f.release_date, f.rate, f.mpa, COUNT(r.id_user) " +
                        "FROM films f " +
                        "LEFT JOIN rating r ON f.id_film = r.id_film " +
                        "GROUP BY f.id_film " +
                        "ORDER BY COUNT(r.id_user) DESC " +
                        "LIMIT ?", limit);
        while (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate")
            );
            film.setMpa(getMpa(filmRows.getInt("mpa")));
            film.setGenres(getGenres(film));
            listRate.add(Optional.of(film));
        }
        return listRate;
    }

    public Mpa getMpa(int idMpa) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("SELECT name FROM mpa_rating WHERE id_rate=?", idMpa);
        if (rows.next()) {
            return new Mpa(idMpa, rows.getString(1));
        } else {
            return null;
        }
    }

    public TreeSet<Genre> getGenres(Film film) {
        ArrayList<Genre> list = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM genres g " +
                        "JOIN film_genre_link f on g.id_genre = f.id_genre"
                        + " WHERE f.id_film=? " +
                        "ORDER BY f.id_genre", film.getId());
        while (rows.next()) {
            list.add(new Genre(rows.getInt("id_genre"), rows.getString("name")));
        }
        if (list.size() > 0) {
            return new TreeSet<>(list);
        } else {
            return new TreeSet<>();
        }
    }
}