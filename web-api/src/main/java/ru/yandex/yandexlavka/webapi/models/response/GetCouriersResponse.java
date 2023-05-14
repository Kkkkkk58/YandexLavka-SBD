package ru.yandex.yandexlavka.webapi.models.response;

import lombok.Builder;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;

import java.util.List;

@Builder
public record GetCouriersResponse(
        List<CourierDto> couriers,
        Integer limit,
        Integer offset
) {
}
