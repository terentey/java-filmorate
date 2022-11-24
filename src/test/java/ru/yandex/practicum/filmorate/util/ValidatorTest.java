package ru.yandex.practicum.filmorate.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

public class ValidatorTest {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private ValidatorTest(){}

    public static Set<ConstraintViolation<Object>> getViolations(Object o) {
        return VALIDATOR.validate(o);
    }
}
