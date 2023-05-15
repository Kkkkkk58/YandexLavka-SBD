package ru.yandex.yandexlavka.dataaccess.exceptions;

import ru.yandex.yandexlavka.common.exceptions.YandexLavkaException;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;

public class OrderException extends YandexLavkaException {

    private OrderException(String message) {
        super(message);
    }

    public static OrderException deliveryIntervalAlreadyExists(Long orderId, TimeInterval deliveryInterval) {
        return new OrderException("Order " + orderId + " already has delivery interval " + deliveryInterval);
    }

    public static OrderException deliveryIntervalDoesNotExist(Long orderId, TimeInterval deliveryInterval) {
        return new OrderException("Order " + orderId + " didn't have delivery interval " + deliveryInterval);
    }
}
