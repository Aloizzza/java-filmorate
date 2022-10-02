package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Component
public class FilmDbStorage implements FilmStorage {
    private static final LocalDate LOW_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;
    private int filmId = 0;

    private static final String SQL_INS_FILMS =
            "INSERT INTO films(id_film,name,description,releasedate,duration,rate,mpa) "
                    + "values(?,?,?,?,?,?,?)";
    private static final String SQL_DEL_GENRE_LINK = "DELETE FROM FILM_GENRE_LINK WHERE ID_FILM=?";
    private static final String SQL_UPD_FILM =
            "MERGE INTO films (ID_FILM, name, description, releasedate, duration, rate,mpa) KEY (ID_FILM) "
                    + "VALUES (?,?, ?, ?, ?, ?, ?)";
    private static final String SQL_MERGE_GENRE_LINK =
            "MERGE INTO FILM_GENRE_LINK (ID_FILM, ID_GENRE) KEY (ID_FILM, ID_GENRE) VALUES (?, ?)";
    private static final String SQL_DEL_FILM =
            "DELETE FROM films WHERE id_film=?;DELETE FROM FILM_GENRE_LINK WHERE ID_FILM=?";
    private static final String SQL_DEL_FILM_ALL = "DELETE FROM FILM_GENRE_LINK; DELETE FROM rating; DELETE FROM films";
    private static final String SQL_INS_RATING = "INSERT INTO rating(id_user,id_film) VALUES(?,?)";
    private static final String SQL_DEL_RATING = "DELETE FROM rating WHERE id_user=? AND id_film=?";


    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private int getFilmId() {
        return ++filmId;
    }


    @Override
    public Optional<Film> create(Film film) {

        int id = getFilmId();

        jdbcTemplate.update(SQL_INS_FILMS, id, film.getName(), film.getDescription(), film.getReleaseDate()
                , film.getDuration()
                , film.getRate(), film.getMpa().getId());
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(
                "SELECT f.id_film id_film,m.name name FROM films f JOIN mpa_rating m ON f.mpa=m.id_rate "
                        + " WHERE f.id_film=?", id);
        if (mpaRow.next()) {
            film.getMpa().setName(mpaRow.getString("name"));
            film.setId(mpaRow.getInt("id_film"));
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(SQL_MERGE_GENRE_LINK, film.getId(), genre.getId());
            }
        }

        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        if (film.getId() <= 0 || film.getId() == null) {
            throw new NotFoundException("id должен быть > 0");
        }
        if (getById(film.getId()).isEmpty()) {
            throw new NotFoundException("фильм для update не найден");
        }
        if (film.getMpa().getId() == null) {
            throw new BadRequestException("фильм должен иметь рейтинг MPA");
        }
        jdbcTemplate.update(SQL_DEL_GENRE_LINK, film.getId());
        jdbcTemplate.update(SQL_UPD_FILM, film.getId(), film.getName(), film.getDescription()
                , film.getReleaseDate(), film.getDuration()
                , film.getRate(), film.getMpa().getId());
        film.setMpa(getMpa(film.getMpa().getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(SQL_MERGE_GENRE_LINK, film.getId(), genre.getId());
            }
            film.setGenres(getGenres(film));
        }
        return Optional.of(film);
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        Mpa mpa;
        SqlRowSet filmRows =
                jdbcTemplate.queryForRowSet("SELECT f.ID_FILM,f.NAME FNAME,f.DESCRIPTION,f.RELEASEDATE,f.DURATION "
                        + ",f.RATE,f.MPA,m.ID_RATE,m.NAME MNAME FROM films f JOIN mpa_rating m ON m.id_rate=f.mpa");
        while (filmRows.next()) {
            int idFilm = filmRows.getInt("id_film");
            mpa = new Mpa(filmRows.getInt("id_rate"), filmRows.getString("MNAME"));

            Film film = new Film(
                    idFilm,
                    filmRows.getString("FNAME"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
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
        jdbcTemplate.update(SQL_DEL_FILM, id, id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(SQL_DEL_FILM_ALL);
        filmId = 0;
    }

    @Override
    public Optional<Film> getById(Integer id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("фильм не найден");
        }
        Mpa mpa = null;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id_film=?", id);
        if (filmRows.next()) {

            Film film = new Film(
                    filmRows.getInt("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
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
    public void addLike(int idUser, int idFilm) {
        jdbcTemplate.update(SQL_INS_RATING, idUser, idFilm);
    }

    @Override
    public void removeLike(int idUser, int idFilm) {
        jdbcTemplate.update(SQL_DEL_RATING, idUser, idFilm);
    }

    @Override
    public List<Optional<Film>> getPopular(Integer limit) {
        List<Optional<Film>> listRate = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.ID_FILM,f.NAME,f.DESCRIPTION,f.DURATION,f.RELEASEDATE,f.RATE,f.MPA,COUNT(r.ID_USER) "
                        + "FROM films f LEFT JOIN RATING r ON f.ID_FILM = r.ID_FILM "
                        + "GROUP BY f.ID_FILM ORDER BY COUNT(r.ID_USER) DESC LIMIT ?"
                , limit);
        while (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("id_film"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releasedate").toLocalDate(),
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
        SqlRowSet rows = jdbcTemplate.queryForRowSet("SELECT NAME FROM MPA_RATING WHERE ID_RATE=?", idMpa);
        if (rows.next()) {
            return new Mpa(idMpa, rows.getString(1));
        } else {
            return null;
        }
    }

    public TreeSet<Genre> getGenres(Film film) {
        ArrayList<Genre> list = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM GENRES g JOIN FILM_GENRE_LINK fgl on g.ID_GENRE = fgl.ID_GENRE"
                        + " WHERE fgl.ID_FILM=? ORDER BY fgl.ID_GENRE"
                , film.getId());
        while (rows.next()) {
            list.add(new Genre(rows.getInt("id_genre"), rows.getString("name")));
        }
        if (list.size() > 0) {
            return new TreeSet<>(list);
        } else {
            return null;
        }
    }
}