package ru.yandex.yandexlavka.service.dto.courier;

import lombok.Builder;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.util.Set;

@Builder
public record CourierDto(

        Long id,
        CourierType type,
        Set<Integer> regions,
        Set<LocalTimeInterval> workingHours
) {
}
