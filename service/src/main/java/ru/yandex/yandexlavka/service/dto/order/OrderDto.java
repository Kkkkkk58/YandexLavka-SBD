package ru.yandex.yandexlavka.service.dto.order;

import lombok.Builder;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record OrderDto(
        Long id,
        float weight,
        int regions,
        Set<TimeInterval> deliveryHours,
        int cost,
        LocalDateTime completedTime
) {
}
