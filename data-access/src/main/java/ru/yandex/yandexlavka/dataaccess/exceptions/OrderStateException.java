package ru.yandex.yandexlavka.dataaccess.exceptions;

import ru.yandex.yandexlavka.common.exceptions.YandexLavkaException;

import java.time.LocalDateTime;

public class OrderStateException extends YandexLavkaException {

    private OrderStateException(String message) {
        super(message);
    }

    public static OrderStateException updateTimestampIsAfterCurrent(LocalDateTime current, LocalDateTime afterUpdate) {
        return new OrderStateException("Update time " + afterUpdate + " is after latest update " + current);
    }
}
