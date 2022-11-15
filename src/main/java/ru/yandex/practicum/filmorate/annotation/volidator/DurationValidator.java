package ru.yandex.practicum.filmorate.annotation.volidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.DurationOfFilm;

public class DurationValidator implements ConstraintValidator<DurationOfFilm, java.time.Duration> {
    @Override
    public boolean isValid(java.time.Duration duration, ConstraintValidatorContext constraintValidatorContext) {
        return !duration.isNegative();
    }
}
