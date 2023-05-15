package ru.yandex.yandexlavka.service.api;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;
import ru.yandex.yandexlavka.service.dto.courier.CourierMetaInfoDto;
import ru.yandex.yandexlavka.service.dto.courier.CreateCourierDto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface CourierService {

    List<CourierDto> create(Collection<CreateCourierDto> createCourierDtos);

    CourierDto getById(Long id);

    List<CourierDto> get(Pageable pageable);

    CourierMetaInfoDto getMetaInformation(Long courierId, LocalDate startDate, LocalDate endDate);
}
