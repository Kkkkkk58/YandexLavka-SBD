package ru.yandex.yandexlavka.webapi.models.response;

import lombok.Builder;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.util.Set;

@Builder
public record GetCourierMetaInfoResponse(
        Long id,
        CourierType type,
        Set<Integer> regions,
        Set<LocalTimeInterval> workingHours,
        Integer rating,
        Integer earnings
) {
}
