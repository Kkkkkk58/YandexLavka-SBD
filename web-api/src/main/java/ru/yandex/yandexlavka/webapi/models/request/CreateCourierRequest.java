package ru.yandex.yandexlavka.webapi.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.yandex.yandexlavka.service.dto.courier.CreateCourierDto;

import java.util.Set;

public record CreateCourierRequest(
        @NotNull Set<@Valid CreateCourierDto> couriers) {
}
