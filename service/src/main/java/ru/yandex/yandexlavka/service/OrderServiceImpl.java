package ru.yandex.yandexlavka.service;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.dataaccess.repositories.CourierRepository;
import ru.yandex.yandexlavka.dataaccess.repositories.OrderRepository;
import ru.yandex.yandexlavka.service.api.OrderService;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrder;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.service.dto.order.CreateOrderDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;
import ru.yandex.yandexlavka.service.exceptions.EntityException;
import ru.yandex.yandexlavka.service.mapping.OrderMappingExtensions;
import ru.yandex.yandexlavka.service.mapping.StreamMappingExtensions;
import ru.yandex.yandexlavka.service.validation.service.api.ValidationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({OrderMappingExtensions.class, StreamMappingExtensions.class})
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final ValidationService validationService;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            CourierRepository courierRepository,
            ValidationService validationService) {

        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.validationService = validationService;
    }

    @Override
    @Transactional
    public List<OrderDto> create(Collection<CreateOrderDto> createOrderDtos) {

        validationService.validate(createOrderDtos);

        List<Order> orders = createOrderDtos.stream().map(
                dto -> Order.builder()
                        .weight(dto.weight())
                        .regions(dto.regions())
                        .deliveryHours(new HashSet<>(dto.deliveryHours()))
                        .cost(dto.cost())
                        .build()
        ).toList();

        return orderRepository.saveAllAndFlush(orders).stream().asOrderDto().toList();
    }

    @Override
    public OrderDto getById(Long id) {
        return getOrder(id).asDto();
    }

    @Override
    public List<OrderDto> get(Pageable pageable) {

        return orderRepository.findAll(pageable).stream().asOrderDto().toList();
    }

    @Override
    @Transactional
    public List<OrderDto> completeOrders(CompleteOrderRequestDto completeOrderRequestDto) {

        validationService.validate(completeOrderRequestDto);

        List<Order> orders = new ArrayList<>();
        for (CompleteOrder completeOrder : completeOrderRequestDto.completeInfo()) {
            Order order = getOrder(completeOrder.orderId());
            Courier courier = getCourier(completeOrder.courierId());

            order.proceed(completeOrder.completeTime(), courier);
            orders.add(order);
        }

        return orderRepository.saveAllAndFlush(orders).stream().asOrderDto().toList();
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> EntityException.entityNotFound(Order.class, id));
    }

    private Courier getCourier(Long id) {
        return courierRepository.findById(id).orElseThrow(() -> EntityException.entityNotFound(Courier.class, id));
    }
}
