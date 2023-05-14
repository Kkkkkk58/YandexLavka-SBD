package ru.yandex.yandexlavka.service.mapping;

import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.dataaccess.models.OrderStatus;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;

import java.time.LocalDateTime;

@UtilityClass
public class OrderMappingExtensions {

    public OrderDto asDto(Order order) {
        LocalDateTime completedTime = (order.getState().getStatus() == OrderStatus.COMPLETED) ? order.getState().getTimestamp() : null;
        return OrderDto.builder()
                .id(order.getId())
                .weight(order.getWeight())
                .regions(order.getRegions())
                .deliveryHours(order.getDeliveryHours())
                .cost(order.getCost())
                .completedTime(completedTime)
                .build();
    }
}
