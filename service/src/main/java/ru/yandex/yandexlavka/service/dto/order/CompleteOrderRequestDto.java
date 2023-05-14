package ru.yandex.yandexlavka.service.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CompleteOrderRequestDto(
        @NotNull Set<@Valid CompleteOrder> completeInfo
) {
}
