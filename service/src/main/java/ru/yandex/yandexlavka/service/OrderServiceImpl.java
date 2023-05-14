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
import ru.yandex.yandexlavka.dataaccess.repositories.OrderStateRepository;
import ru.yandex.yandexlavka.service.api.OrderService;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrder;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.service.dto.order.CreateOrderDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;
import ru.yandex.yandexlavka.service.mapping.OrderMappingExtensions;
import ru.yandex.yandexlavka.service.mapping.StreamMappingExtensions;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({OrderMappingExtensions.class, StreamMappingExtensions.class})
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CourierRepository courierRepository) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
    }

    @Override
    @Transactional
    public List<OrderDto> create(Collection<CreateOrderDto> createOrderDtos) {

        List<Order> orders = createOrderDtos.stream().map(
                dto -> Order.builder()
                        .weight(dto.weight())
                        .regions(dto.regions())
                        .deliveryHours(dto.deliveryHours())
                        .cost(dto.cost())
                        .build()
        ).toList();

        return orderRepository.saveAllAndFlush(orders).stream().asOrderDto().toList();
    }

    @Override
    public OrderDto getById(Long id) {
        // TODO
        return orderRepository.findById(id).orElseThrow().asDto();
    }

    @Override
    public List<OrderDto> get(Pageable pageable) {

        return orderRepository.findAll(pageable).stream().asOrderDto().toList();
    }

    @Override
    @Transactional
    public OrderDto completeOrder(CompleteOrder completeOrder) {

        Order order = orderRepository.findById(completeOrder.orderId()).orElseThrow();
        Courier courier = courierRepository.findById(completeOrder.courierId()).orElseThrow();

        order.proceed(LocalDateTime.now(), courier);
        return orderRepository.save(order).asDto();
    }

    @Override
    @Transactional
    public List<OrderDto> completeOrders(CompleteOrderRequestDto completeOrderRequestDto) {

        return completeOrderRequestDto.completeInfo().stream().map(this::completeOrder).toList();
    }
}
