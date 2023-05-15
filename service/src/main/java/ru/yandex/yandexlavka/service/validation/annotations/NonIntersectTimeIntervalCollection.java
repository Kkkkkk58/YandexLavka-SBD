package ru.yandex.yandexlavka.service.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.yandexlavka.service.validation.validators.NonIntersectTimeIntervalCollectionValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = NonIntersectTimeIntervalCollectionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonIntersectTimeIntervalCollection {
    String message() default "Time interval collection has intersections";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
