package ru.yandex.yandexlavka.service.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.util.Set;

public record CreateOrderDto(
        @Positive float weight,
        @Positive int regions,
        @NotEmpty Set<@NotNull LocalTimeInterval> deliveryHours,
        @PositiveOrZero int cost
) {
}
