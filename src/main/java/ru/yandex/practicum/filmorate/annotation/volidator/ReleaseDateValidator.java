package ru.yandex.practicum.filmorate.annotation.volidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private static final LocalDate INTERNATIONAL_FILM_DAY = LocalDate.of(1895, 12, 28);
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(INTERNATIONAL_FILM_DAY);
    }
}
