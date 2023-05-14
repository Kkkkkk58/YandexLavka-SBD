package ru.yandex.yandexlavka.webapi.models.response;

import ru.yandex.yandexlavka.webapi.models.validation.Violation;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {
}
