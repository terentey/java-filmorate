package ru.yandex.practicum.filmorate.annotation.volidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.Login;

public class LoginValidator implements ConstraintValidator<Login, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null) return false;
        else return !s.contains(" ");
    }
}
