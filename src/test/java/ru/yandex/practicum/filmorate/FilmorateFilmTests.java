package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmorateFilmTests {

    @Autowired
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController.deleteAll();
    }

    @Test
    public void shouldCreateFilmTest(){
        Film film =
                new Film(1, "Example", "desc Example", LocalDate.of(2000, 1, 1)
                        , 100, 0);
        film.setMpa(new Mpa(1, ""));
        TreeSet<Genre> genres = new TreeSet<>();
        genres.add(new Genre(1, ""));
        film.setGenres(genres);
        filmController.create(film);

        Assertions.assertEquals(filmController.getCountFilms(), 1);
    }

    @Test
    public void shouldNotCreateFilmWithEmptyNameTest() {
        Film film = new Film(1, "", "desc Example", LocalDate.of(2000, 1, 1)
                , 100, 0);

        assertThrows(RuntimeException.class, () -> filmController.create(film));
        film.setName(null);
        assertThrows(NullPointerException.class, () -> filmController.create(film));
    }

    @Test
    public void shouldNotCreateFilmWithTooLongDescriptionTest() {
        String s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut" +
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris" +
                "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit" +
                "esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in" +
                "culpa qui officia deserunt mollit anim id est laborum.";
        Film film = new Film(1, "Example", s, LocalDate.of(2000, 1, 1)
                , 100, 0);

        assertThrows(RuntimeException.class, () -> filmController.create(film));
    }

    @Test
    public void shouldNotCreateFilmWithInvalidDateTest() {
        Film film = new Film(1, "Example", "desc Example"
                , LocalDate.of(1895, 12, 27)
                , 100, 0);
        assertThrows(RuntimeException.class, () -> filmController.create(film));
    }

    @Test
    public void shouldNotCreateFilmWithInvalidDurationTest() {
        Film film = new Film(1, "Example", "desc Example"
                , LocalDate.of(2000, 1, 1), 100, 0);
        assertThrows(RuntimeException.class, () -> filmController.create(film));
        film.setDuration(0);
        assertThrows(RuntimeException.class, () -> filmController.update(film));
    }

    @Test
    public void shouldUpdateFilmTest() {
        Film film = new Film(1, "Example", "desc Example"
                , LocalDate.of(2000, 1, 1)
                , 100, 0);
        film.setMpa(new Mpa(1, ""));
        TreeSet<Genre> genres = new TreeSet<>();
        genres.add(new Genre(1, ""));
        film.setGenres(genres);

        Film filmUpd = new Film(1, "Example2", "desc Example2"
                , LocalDate.of(2002, 1, 1), 100, 0);
        filmUpd.setMpa(new Mpa(1, ""));
        filmUpd.setGenres(genres);

        filmController.create(film);
        filmController.update(filmUpd);
        Assertions.assertEquals(filmController.getCountFilms(), 1);
    }

    @Test
    public void shouldGetFilmsTest(){
        Film film = new Film(1, "Example", "desc Example",
                LocalDate.of(2000, 1, 1), 100, 0);

        Film film2 = new Film(2, "Example", "desc Example"
                , LocalDate.of(2000, 1, 1), 100, 0);
        film.setMpa(new Mpa(1, ""));
        TreeSet<Genre> genres = new TreeSet<>();
        genres.add(new Genre(1, ""));
        film.setGenres(genres);
        film2.setMpa(new Mpa(1, ""));
        film2.setGenres(genres);

        filmController.create(film);
        filmController.create(film2);

        List<Film> films = filmController.getFilms();

        Assertions.assertEquals(films.size(), 2);
    }
}