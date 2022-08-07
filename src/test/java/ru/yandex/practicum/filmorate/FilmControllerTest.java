package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.InvalidFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController();
    Film testFilm = new Film();
    Film testFilm2 = new Film();

    @Test
    public void shouldBeEmpty() {
        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    void shouldAddFilm() throws InvalidFilmException {
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Американский фильм-детектив 1999 года, снятый режиссёром Джоном Мактирнаном");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.create(testFilm);

        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void shouldNotAddFilmWithEmptyName() {
        testFilm2.setName("");
        testFilm2.setDescription("Жизнь банкира превращается в ад, но, возможно, это кем-то спланировано." +
                "Хищный и едкий триллер Дэвида Финчера");
        testFilm2.setReleaseDate(LocalDate.of(1997, 9, 12));
        testFilm2.setDuration(128);

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.create(testFilm2)
        );
        assertEquals("Название фильма не может быть пустым.", e.getMessage());
    }

    @Test
    void shouldNotAddFilmWithDescriptionLongerThan200() {
        testFilm2.setName("Игра");
        testFilm2.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation" +
                "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in" +
                "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non" +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        testFilm2.setReleaseDate(LocalDate.of(1997, 9, 12));
        testFilm2.setDuration(128);

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.create(testFilm2)
        );
        assertEquals("Максимальная длина описания — 200 символов.", e.getMessage());
    }

    @Test
    void shouldNotAddFilmWithDateEarlierThan1895December28() {
        testFilm2.setName("Игра");
        testFilm2.setDescription("Жизнь банкира превращается в ад, но, возможно, это кем-то спланировано." +
                "Хищный и едкий триллер Дэвида Финчера");
        testFilm2.setReleaseDate(LocalDate.of(1800, 9, 12));
        testFilm2.setDuration(128);

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.create(testFilm2)
        );
        assertEquals("Дата релиза фильма не может быть раньше изобретения кино.", e.getMessage());
    }

    @Test
    void shouldNotAddFilmWithNegativeDuration() {
        testFilm2.setName("Игра");
        testFilm2.setDescription("Жизнь банкира превращается в ад, но, возможно, это кем-то спланировано." +
                "Хищный и едкий триллер Дэвида Финчера");
        testFilm2.setReleaseDate(LocalDate.of(1997, 9, 12));
        testFilm2.setDuration(-128);

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.create(testFilm2)
        );
        assertEquals("Продолжительность фильма должна быть положительной.", e.getMessage());
    }

    @Test
    void shouldUpdateFilm() throws InvalidFilmException {
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Американский фильм-детектив 1999 года, снятый режиссёром Джоном Мактирнаном");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.create(testFilm);

        testFilm.setId(1);
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Кто вообще читает описание?");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.update(testFilm);

        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void shouldNotUpdateFilmWithNonExistentId() throws InvalidFilmException {
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Американский фильм-детектив 1999 года, снятый режиссёром Джоном Мактирнаном");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.create(testFilm);

        testFilm.setId(2);
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Кто вообще читает описание?");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.update(testFilm)
        );
        assertEquals("Фильм с указанным id не существует.", e.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithEmptyName() {
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Американский фильм-детектив 1999 года, снятый режиссёром Джоном Мактирнаном");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.create(testFilm);

        testFilm.setName("");

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.update(testFilm)
        );
        assertEquals("Название фильма не может быть пустым.", e.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithDescriptionLongerThan200() {
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Американский фильм-детектив 1999 года, снятый режиссёром Джоном Мактирнаном");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.create(testFilm);

        testFilm.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation" +
                "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in" +
                "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non" +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.update(testFilm)
        );
        assertEquals("Максимальная длина описания — 200 символов.", e.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithDateEarlierThan1895December28() {
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Американский фильм-детектив 1999 года, снятый режиссёром Джоном Мактирнаном");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.create(testFilm);

        testFilm.setReleaseDate(LocalDate.of(1800, 9, 12));

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.update(testFilm)
        );
        assertEquals("Дата релиза фильма не может быть раньше изобретения кино.", e.getMessage());
    }

    @Test
    void shouldNotUpdateFilmWithNegativeDuration() {
        testFilm.setName("Афера Томаса Крауна");
        testFilm.setDescription("Американский фильм-детектив 1999 года, снятый режиссёром Джоном Мактирнаном");
        testFilm.setReleaseDate(LocalDate.of(1999, 8, 6));
        testFilm.setDuration(113);
        filmController.create(testFilm);

        testFilm.setDuration(-113);

        InvalidFilmException e = assertThrows(
                InvalidFilmException.class,
                () -> filmController.update(testFilm)
        );
        assertEquals("Продолжительность фильма должна быть положительной.", e.getMessage());
    }
}