package ru.yandex.yandexlavka.service.dto.order;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CompleteOrder(
        // TODO Check if exists ?
        @Positive Long courierId,
        @Positive Long orderId,
        @PastOrPresent LocalDateTime completeTime
) {
}
