package ru.yandex.yandexlavka.webapi.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.yandex.yandexlavka.service.dto.order.CreateOrderDto;

import java.util.Set;

public record CreateOrderRequest(
        @NotNull Set<@Valid CreateOrderDto> orders
) {
}
