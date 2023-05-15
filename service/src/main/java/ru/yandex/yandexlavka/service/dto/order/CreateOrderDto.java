package ru.yandex.yandexlavka.service.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.service.validation.annotations.NonIntersectTimeIntervalCollection;

import java.util.Set;

public record CreateOrderDto(
        @Positive float weight,
        @Positive int regions,

        @JsonProperty(value = "delivery_hours")
        @NonIntersectTimeIntervalCollection Set<TimeInterval> deliveryHours,
        @PositiveOrZero int cost
) {
}
