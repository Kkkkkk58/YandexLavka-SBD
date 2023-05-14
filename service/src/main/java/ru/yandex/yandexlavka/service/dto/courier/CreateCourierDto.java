package ru.yandex.yandexlavka.service.dto.courier;

import jakarta.validation.constraints.NotNull;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.util.Set;

public record CreateCourierDto(
        CourierType courierType,
        Set<@NotNull Integer> regions,
        Set<@NotNull LocalTimeInterval> workingHours
) {
}
