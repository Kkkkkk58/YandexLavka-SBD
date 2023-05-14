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
import ru.yandex.yandexlavka.service.mapping.CourierMappingExtensions;
import ru.yandex.yandexlavka.service.mapping.StreamMappingExtensions;

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

    @Autowired
    public CourierServiceImpl(CourierRepository courierRepository,
                              OrderStateRepository orderStateRepository) {
        this.courierRepository = courierRepository;
        this.orderStateRepository = orderStateRepository;
    }

    @Override
    @Transactional
    public List<CourierDto> create(Collection<CreateCourierDto> createCourierDtos) {

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
        Courier courier = courierRepository.findById(id).orElseThrow(() -> new RuntimeException("//TODO"));
        return courier.asDto();
    }

    @Override
    public List<CourierDto> get(Pageable pageable) {
        return courierRepository.findAll(pageable).stream().asCourierDto().toList();
    }

    @Override
    public CourierMetaInfoDto getRatings(Long courierId, LocalDate startDate, LocalDate endDate) {
        Courier courier = courierRepository.findById(courierId).orElseThrow();

        return CourierMetaInfoDto.builder()
                .id(courier.getId())
                .type(courier.getType())
                .regions(courier.getRegions())
                .workingHours(courier.getWorkingHours())
                .rating(getRating(courier, startDate, endDate))
                .earnings(getEarnings(courier, startDate, endDate))
                .build();
    }

    private Integer getEarnings(Courier courier, LocalDate startDate, LocalDate endDate) {
        List<OrderState> orderStates = orderStateRepository.findByTimestampBetweenAndCourierAndStatus(startDate.atStartOfDay(), endDate.atStartOfDay(), courier, OrderStatus.DELIVERED);
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
        long completedOrders = orderStateRepository.findByTimestampBetweenAndCourierAndStatus(startDate.atStartOfDay(), endDate.atStartOfDay(), courier, OrderStatus.DELIVERED).size();

        if (hours == 0 || completedOrders == 0) {
            return null;
        }

        return Math.toIntExact(courier.getType().getRatingCoefficient() * completedOrders / hours);
    }

}
