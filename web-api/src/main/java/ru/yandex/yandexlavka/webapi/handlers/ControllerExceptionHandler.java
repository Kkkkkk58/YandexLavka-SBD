package ru.yandex.yandexlavka.webapi.handlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.yandexlavka.common.exceptions.YandexLavkaException;
import ru.yandex.yandexlavka.service.exceptions.EntityException;
import ru.yandex.yandexlavka.webapi.models.response.BadRequestResponse;
import ru.yandex.yandexlavka.webapi.models.response.NotFoundResponse;
import ru.yandex.yandexlavka.webapi.models.response.UnprocessableEntityResponse;
import ru.yandex.yandexlavka.webapi.models.response.ValidationErrorResponse;
import ru.yandex.yandexlavka.webapi.models.validation.Violation;

import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<NotFoundResponse> handleEntityNotFoundException(EntityException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new NotFoundResponse());
    }

    @ExceptionHandler(YandexLavkaException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<UnprocessableEntityResponse> handleDomainException(YandexLavkaException exception) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new UnprocessableEntityResponse(exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleConstraintValidationException(
            ConstraintViolationException exception) {

        List<Violation> violations = exception.getConstraintViolations().stream().map(
                constraintViolation -> new Violation(
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()
                )
        ).toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(violations));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentsValidationsException(
            MethodArgumentNotValidException exception) {

        List<Violation> violations = exception.getBindingResult().getFieldErrors().stream().map(
                fieldError -> new Violation(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                )
        ).toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BadRequestResponse> handleIllegalArgumentExceptionHandler(IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BadRequestResponse());
    }
}
