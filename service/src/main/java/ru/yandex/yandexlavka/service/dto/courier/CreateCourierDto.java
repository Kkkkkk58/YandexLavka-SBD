package ru.yandex.yandexlavka.service.dto.courier;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.util.Set;

public record CreateCourierDto(
        @NotNull CourierType courierType,
        @NotNull Set<@Positive Integer> regions,
        @NotNull Set<@NotNull LocalTimeInterval> workingHours
) {
}
