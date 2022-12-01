package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleIncorrectId(IncorrectIdException e) {
        log.error("Неверный id");
        return getResponseEntity(NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("Невалидное значение, переданное в контролер");
        return getResponseEntity(BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> unhandledErrors(Throwable e) {
        log.error("Необработанное исключение");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleInvalidData(ConstraintViolationException e) {
        log.error("Невалидное значение");
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    private ResponseEntity<Map<String,String>> getResponseEntity(HttpStatus status) {
        return new ResponseEntity<>(Map.of("timestamp", LocalDateTime.now().toString(),
                "status", status.toString(),
                "error", status.getReasonPhrase(),
                "path", ServletUriComponentsBuilder.fromCurrentRequest().toUriString()),
                status);
    }
}
