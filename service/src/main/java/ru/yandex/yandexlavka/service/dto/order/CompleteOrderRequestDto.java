package ru.yandex.yandexlavka.service.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CompleteOrderRequestDto(
        @JsonProperty(value = "complete_info")
        @NotNull Set<@Valid CompleteOrder> completeInfo
) {
}
