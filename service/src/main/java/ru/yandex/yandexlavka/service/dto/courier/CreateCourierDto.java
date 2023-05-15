package ru.yandex.yandexlavka.service.dto.courier;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.service.validation.annotations.NonIntersectTimeIntervalCollection;

import java.util.Set;

public record CreateCourierDto(
        @JsonProperty(value = "courier_type")
        @NotNull CourierType courierType,
        @NotNull Set<@Positive Integer> regions,
        @JsonProperty(value = "working_hours")
        @NonIntersectTimeIntervalCollection Set<TimeInterval> workingHours
) {
}
