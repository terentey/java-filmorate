package ru.yandex.practicum.filmorate.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class IncorrectIdException extends RuntimeException {
}
