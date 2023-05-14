package ru.yandex.yandexlavka.service.mapping;

import lombok.experimental.UtilityClass;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;

import java.util.stream.Stream;

@UtilityClass
public class StreamMappingExtensions {

    public static Stream<CourierDto> asCourierDto(Stream<Courier> couriers) {
        return couriers.map(CourierMappingExtensions::asDto);
    }

    public static Stream<OrderDto> asOrderDto(Stream<Order> orders) {
        return orders.map(OrderMappingExtensions::asDto);
    }
}
