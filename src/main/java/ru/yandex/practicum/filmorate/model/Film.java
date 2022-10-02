package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.constraint.DateAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.TreeSet;

@Data
public class Film {
    private Integer id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @DateAfter(date = "1895-12-28", message = "Дата релиза фильма не может быть раньше изобретения кино.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private int duration;

    private int rate;

    @NotNull(message = "Укажите MPA для фильма")
    private Mpa mpa;
    private TreeSet<Genre> genres;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }
}
