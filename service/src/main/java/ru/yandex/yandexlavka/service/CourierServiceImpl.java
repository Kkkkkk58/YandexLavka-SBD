package ru.yandex.yandexlavka.service;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.orderstates.OrderState;
import ru.yandex.yandexlavka.dataaccess.models.OrderStatus;
import ru.yandex.yandexlavka.dataaccess.repositories.CourierRepository;
import ru.yandex.yandexlavka.dataaccess.repositories.OrderStateRepository;
import ru.yandex.yandexlavka.service.api.CourierService;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;
import ru.yandex.yandexlavka.service.dto.courier.CourierMetaInfoDto;
import ru.yandex.yandexlavka.service.dto.courier.CreateCourierDto;
import ru.yandex.yandexlavka.service.exceptions.EntityException;
import ru.yandex.yandexlavka.service.mapping.CourierMappingExtensions;
import ru.yandex.yandexlavka.service.mapping.StreamMappingExtensions;
import ru.yandex.yandexlavka.service.validation.service.api.ValidationService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({CourierMappingExtensions.class, StreamMappingExtensions.class})
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final OrderStateRepository orderStateRepository;
    private final ValidationService validationService;

    @Autowired
    public CourierServiceImpl(
            CourierRepository courierRepository,
            OrderStateRepository orderStateRepository,
            ValidationService validationService) {

        this.courierRepository = courierRepository;
        this.orderStateRepository = orderStateRepository;
        this.validationService = validationService;
    }

    @Override
    @Transactional
    public List<CourierDto> create(Collection<CreateCourierDto> createCourierDtos) {

        validationService.validate(createCourierDtos);

        List<Courier> couriers = createCourierDtos.stream().map(
                dto -> Courier.builder()
                        .type(dto.courierType())
                        .regions(dto.regions())
                        .workingHours(dto.workingHours())
                        .build()
        ).toList();

        return courierRepository.saveAllAndFlush(couriers).stream().asCourierDto().toList();
    }

    @Override
    public CourierDto getById(Long id) {
        return getCourier(id).asDto();
    }

    @Override
    public List<CourierDto> get(Pageable pageable) {
        return courierRepository.findAll(pageable).stream().asCourierDto().toList();
    }

    @Override
    public CourierMetaInfoDto getRatings(Long courierId, LocalDate startDate, LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date can't be before start date");
        }

        Courier courier = getCourier(courierId);

        return CourierMetaInfoDto.builder()
                .id(courier.getId())
                .type(courier.getType())
                .regions(courier.getRegions())
                .workingHours(courier.getWorkingHours())
                .rating(getRating(courier, startDate, endDate))
                .earnings(getEarnings(courier, startDate, endDate))
                .build();
    }

    private Courier getCourier(Long id) {
        return courierRepository.findById(id).orElseThrow(() -> EntityException.entityNotFound(Courier.class, id));
    }

    private Integer getEarnings(Courier courier, LocalDate startDate, LocalDate endDate) {
        List<OrderState> orderStates = orderStateRepository.findByTimestampBetweenAndCourierAndStatus(startDate.atStartOfDay(), endDate.atStartOfDay(), courier, OrderStatus.COMPLETED);
        if (orderStates.isEmpty()) {
            return null;
        }

        long sum = orderStates.stream()
                .map(orderState -> orderState.getOrder().getCost())
                .mapToInt(Integer::intValue)
                .sum();

        return Math.toIntExact(courier.getType().getSalaryCoefficient() * sum);
    }

    private Integer getRating(Courier courier, LocalDate startDate, LocalDate endDate) {
        long hours = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toHours();
        long completedOrders = orderStateRepository.findByTimestampBetweenAndCourierAndStatus(startDate.atStartOfDay(), endDate.atStartOfDay(), courier, OrderStatus.COMPLETED).size();

        if (hours == 0 || completedOrders == 0) {
            return null;
        }

        return Math.toIntExact(courier.getType().getRatingCoefficient() * (completedOrders / hours));
    }

}
