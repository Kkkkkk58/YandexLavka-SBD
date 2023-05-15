package ru.yandex.yandexlavka.service.dto.courier;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;

import java.util.Set;

@Builder
public record CourierMetaInfoDto(
        Long id,
        CourierType type,
        Set<Integer> regions,
        Set<TimeInterval> workingHours,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer rating,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer earnings
) {
}
