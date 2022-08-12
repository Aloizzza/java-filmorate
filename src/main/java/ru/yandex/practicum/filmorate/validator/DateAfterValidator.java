package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.constraint.DateAfter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {
    String date;

    @Override
    public void initialize(DateAfter dateAfter) {
        this.date = dateAfter.date();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            LocalDate parsedAfterDate = LocalDate.parse(date);
            return value.isAfter(parsedAfterDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
