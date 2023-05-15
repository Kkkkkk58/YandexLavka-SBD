package ru.yandex.yandexlavka.service.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CompleteOrder(
        @JsonProperty(value = "courier_id")
        @Positive Long courierId,

        @JsonProperty(value = "order_id")
        @Positive Long orderId,

        @JsonProperty(value = "complete_time")
        @PastOrPresent LocalDateTime completeTime
) {
}
