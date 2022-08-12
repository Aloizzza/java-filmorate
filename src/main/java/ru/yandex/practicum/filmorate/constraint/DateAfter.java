package ru.yandex.practicum.filmorate.constraint;

import ru.yandex.practicum.filmorate.validator.DateAfterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DateAfterValidator.class)
@Documented
public @interface DateAfter {

    String date();

    String message() default "{Date.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}