package ru.yandex.practicum.filmorate.exception;

public class ErrorResponse {
    String e;
    String description;

    public ErrorResponse(String e, String description) {
        this.e = e;
        this.description = description;
    }

    public String getE() {
        return e;
    }

    public String getDescription() {
        return description;
    }
}
