package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.constraint.DateAfter;
import ru.yandex.practicum.filmorate.exception.ConflictException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Film {
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @DateAfter(date = "1895-12-28", message = "Дата релиза фильма не может быть раньше изобретения кино.")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private int duration;

    private Set<Integer> usersLikes = new HashSet<>();

    public void addLike(int id) {
        if (usersLikes.contains(id)) {
            throw new ConflictException("У пользователя уже есть лайк на фильме с id: " + id);
        }
        usersLikes.add(id);
    }

    public void removeLike(int id) {
        if (!usersLikes.contains(id)) {
            throw new ConflictException("У пользователя нет лайка на фильме с id: " + id);
        }
        usersLikes.remove(id);
    }

    public Integer getLikes(int id) {
        return usersLikes.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
