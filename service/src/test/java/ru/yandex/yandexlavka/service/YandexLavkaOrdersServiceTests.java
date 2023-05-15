package ru.yandex.yandexlavka.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.OrderStatus;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.dataaccess.repositories.CourierRepository;
import ru.yandex.yandexlavka.dataaccess.repositories.OrderRepository;
import ru.yandex.yandexlavka.service.api.OrderService;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrder;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;
import ru.yandex.yandexlavka.service.validation.service.api.ValidationService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class YandexLavkaOrdersServiceTests {

    private OrderRepository orderRepository;
    private CourierRepository courierRepository;

    private OrderService orderService;

    @BeforeEach
    public void setup() {

        orderRepository = mock(OrderRepository.class);
        courierRepository = mock(CourierRepository.class);
        ValidationService validationService = mock(ValidationService.class);

        orderService = new OrderServiceImpl(orderRepository, courierRepository, validationService);
    }

    @Test
    public void getOrderById_returnsOrder() {
        Long orderId = 1L;
        float weight = 3;
        int cost = 5;
        int regions = 58;
        Set<TimeInterval> deliveryHours = Set.of(new TimeInterval(LocalTime.of(13, 0, 0), LocalTime.of(15, 0, 0)));

        Order order = Order.builder()
                .weight(weight)
                .regions(regions)
                .cost(cost)
                .deliveryHours(deliveryHours)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderDto orderDto = orderService.getById(orderId);

        Assertions.assertEquals(weight, orderDto.weight());
        Assertions.assertEquals(cost, orderDto.cost());
        Assertions.assertEquals(regions, orderDto.regions());
        Assertions.assertEquals(deliveryHours, orderDto.deliveryHours());
    }

    @Test
    public void completeOrders_ordersAreCompleted() {
        Order order = getTestOrder();
        Courier courier = getTestCourier();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));

        CompleteOrderRequestDto completeOrderRequest = new CompleteOrderRequestDto(Set.of(
                new CompleteOrder(1L, 1L, LocalDateTime.now())
        ));
        orderService.completeOrders(completeOrderRequest);

        Assertions.assertEquals(OrderStatus.COMPLETED, order.getState().getStatus());
        Assertions.assertEquals(courier, order.getState().getCourier());
        Assertions.assertFalse(courier.getAssociatedOrders().isEmpty());
    }

    private Order getTestOrder() {
        float weight = 3;
        int cost = 5;
        int regions = 58;
        Set<TimeInterval> deliveryHours = Set.of(new TimeInterval(LocalTime.of(13, 0, 0), LocalTime.of(15, 0, 0)));

        return Order.builder()
                .weight(weight)
                .regions(regions)
                .cost(cost)
                .deliveryHours(deliveryHours)
                .build();
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
}
