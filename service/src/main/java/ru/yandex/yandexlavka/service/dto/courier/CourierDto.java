package ru.yandex.yandexlavka.service.dto.courier;

import lombok.Builder;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;

import java.util.Set;

@Builder
public record CourierDto(

        Long id,
        CourierType type,
        Set<Integer> regions,
        Set<TimeInterval> workingHours
) {
}
