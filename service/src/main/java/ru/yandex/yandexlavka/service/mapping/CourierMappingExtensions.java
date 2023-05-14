package ru.yandex.yandexlavka.service.mapping;

import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;

@UtilityClass
public class CourierMappingExtensions {

    public static CourierDto asDto(Courier courier) {
        return CourierDto.builder()
                .id(courier.getId())
                .type(courier.getType())
                .regions(courier.getRegions())
                .workingHours(courier.getWorkingHours())
                .build();
    }
}
