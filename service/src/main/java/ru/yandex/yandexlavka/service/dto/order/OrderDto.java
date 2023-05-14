package ru.yandex.yandexlavka.service.dto.order;

import lombok.Builder;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record OrderDto(
        Long id,
        float weight,
        int regions,
        Set<LocalTimeInterval> deliveryHours,
        int cost,
        LocalDateTime completedTime
) {
}
