package ru.yandex.practicum.filmorate.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

public class ValidatorTest {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private ValidatorTest(){}

    public static Set<ConstraintViolation<Object>> getViolations(Object o) {
        return VALIDATOR.validate(o);
    }
}
