package ru.yandex.yandexlavka.service.api;

import org.springframework.data.domain.Pageable;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrder;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.service.dto.order.CreateOrderDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;

import java.util.Collection;
import java.util.List;

public interface OrderService {

    List<OrderDto> create(Collection<CreateOrderDto> createOrderDtos);

    OrderDto getById(Long id);

    List<OrderDto> get(Pageable pageable);

    OrderDto completeOrder(CompleteOrder completeOrder);

    List<OrderDto> completeOrders(CompleteOrderRequestDto completeOrderRequestDto);
}
