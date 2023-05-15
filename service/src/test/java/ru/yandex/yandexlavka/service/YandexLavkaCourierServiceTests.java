package ru.yandex.yandexlavka.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.dataaccess.entities.orderstates.CompletedOrderState;
import ru.yandex.yandexlavka.dataaccess.entities.orderstates.OrderState;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.OrderStatus;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.dataaccess.repositories.CourierRepository;
import ru.yandex.yandexlavka.dataaccess.repositories.OrderStateRepository;
import ru.yandex.yandexlavka.service.api.CourierService;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;
import ru.yandex.yandexlavka.service.validation.service.api.ValidationService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YandexLavkaCourierServiceTests {

    private CourierRepository courierRepository;
    private OrderStateRepository orderStateRepository;

    private CourierService courierService;

    @BeforeEach
    public void setup() {
        courierRepository = mock(CourierRepository.class);
        orderStateRepository = mock(OrderStateRepository.class);
        ValidationService validationService = mock(ValidationService.class);

        courierService = new CourierServiceImpl(courierRepository, orderStateRepository, validationService);
    }

    @Test
    public void getCourierById_returnsCourier() {

        Long courierId = 1L;
        CourierType courierType = CourierType.BIKE;
        Set<Integer> regions = Set.of(58);
        Set<TimeInterval> workingHours = Set.of(new TimeInterval(LocalTime.of(13, 0, 0), LocalTime.of(15, 0, 0)));

        Courier courier = Courier.builder()
                .type(courierType)
                .regions(regions)
                .workingHours(workingHours)
                .build();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        CourierDto courierDto = courierService.getById(courierId);

        Assertions.assertEquals(courierType, courierDto.type());
        Assertions.assertEquals(regions, courierDto.regions());
        Assertions.assertEquals(workingHours, courierDto.workingHours());
    }

    @Test
    public void getRatingWithOrders_ratingIsEvaluatedCorrectly() {
        Courier courier = getTestCourier();
        int ordersCount = 49;
        List<OrderState> states = getCompletedTestOrders(courier, ordersCount).stream().map(Order::getState).toList();
        LocalDate from = LocalDate.of(2003, 5, 7);
        LocalDate to = LocalDate.of(2003, 5, 9);
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(orderStateRepository.findByTimestampBetweenAndCourierAndStatus(from.atStartOfDay(), to.atStartOfDay(), courier, OrderStatus.COMPLETED)).thenReturn(states);

        int hours = Math.toIntExact(Duration.between(from.atStartOfDay(), to.atStartOfDay()).toHours());
        int expectedRating = courier.getType().getRatingCoefficient() * (ordersCount / hours);
        Assertions.assertEquals(expectedRating, courierService.getMetaInformation(1L, from, to).rating());
    }

    @Test
    public void getRatingWithoutOrders_ratingIsNull() {
        Courier courier = getTestCourier();
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        LocalDate from = LocalDate.of(2003, 5, 7);
        LocalDate to = LocalDate.of(2003, 5, 9);
        when(orderStateRepository.findByTimestampBetweenAndCourierAndStatus(from.atStartOfDay(), to.atStartOfDay(), courier, OrderStatus.COMPLETED)).thenReturn(new ArrayList<>());

        Assertions.assertNull(courierService.getMetaInformation(1L, from, to).rating());
    }

    @Test
    public void getEarningsWithOrders_earningsAreEvaluatedCorrectly() {
        Courier courier = getTestCourier();
        int ordersCount = 49;
        List<Order> orders = getCompletedTestOrders(courier, ordersCount);
        LocalDate from = LocalDate.of(2003, 5, 7);
        LocalDate to = LocalDate.of(2003, 5, 9);
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(orderStateRepository.findByTimestampBetweenAndCourierAndStatus(from.atStartOfDay(), to.atStartOfDay(), courier, OrderStatus.COMPLETED)).thenReturn(orders.stream().map(Order::getState).toList());

        long sum = orders.stream().mapToInt(Order::getCost).sum();
        int expectedEarnings = Math.toIntExact(courier.getType().getSalaryCoefficient() * sum);
        Assertions.assertEquals(expectedEarnings, courierService.getMetaInformation(1L, from, to).earnings());
    }

    @Test
    public void getEarningsWithoutOrders_earningsAreNull() {
        Courier courier = getTestCourier();
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        LocalDate from = LocalDate.of(2003, 5, 7);
        LocalDate to = LocalDate.of(2003, 5, 9);
        when(orderStateRepository.findByTimestampBetweenAndCourierAndStatus(from.atStartOfDay(), to.atStartOfDay(), courier, OrderStatus.COMPLETED)).thenReturn(new ArrayList<>());

        Assertions.assertNull(courierService.getMetaInformation(1L, from, to).earnings());
    }

    private Courier getTestCourier() {
        CourierType courierType = CourierType.BIKE;
        Set<Integer> regions = Set.of(58);
        Set<TimeInterval> workingHours = Set.of(new TimeInterval(LocalTime.of(13, 0, 0), LocalTime.of(15, 0, 0)));

        return Courier.builder()
                .type(courierType)
                .regions(regions)
                .workingHours(workingHours)
                .build();
    }

    private List<Order> getCompletedTestOrders(Courier courier, int ordersCount) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < ordersCount; ++i) {
            Order order = new Order((long) (i + 1), i, i, new HashSet<>(), i);
            order.setState(new CompletedOrderState(order, LocalDateTime.of(2003, 5, 7, 1, 1, 1), courier));
            orders.add(order);
        }

        return orders;
    }
}
