package ru.yandex.yandexlavka.service.mapping;

import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;

@UtilityClass
public class OrderMappingExtensions {

    public OrderDto asDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .weight(order.getWeight())
                .regions(order.getRegions())
                .deliveryHours(order.getDeliveryHours())
                .cost(order.getCost())
                .stateTimestamp(order.getState().getTimestamp())
                .build();
    }
}
