package ru.yandex.yandexlavka.dataaccess.exceptions;

import ru.yandex.yandexlavka.common.exceptions.YandexLavkaException;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

public class CourierException extends YandexLavkaException {

    private CourierException(String message) {
        super(message);
    }

    public static CourierException regionAlreadyCovered(Long courierId, Integer region) {
        return new CourierException("Region " + region + " is already covered by courier " + courierId);
    }

    public static CourierException regionDoesNotExist(Long courierId, Integer region) {
        return new CourierException("Courier " + courierId + " has nothing to do with region " + region);
    }


    public static CourierException workingIntervalAlreadyExists(Long courierId, LocalTimeInterval workingInterval) {
        return new CourierException("Courier " + courierId + " already has " + workingInterval + " among work hours");
    }

    public static CourierException workingIntervalDoesNotExist(Long courierId, LocalTimeInterval workingInterval) {
        return new CourierException("Courier " + courierId + " doesn't work at " + workingInterval);
    }


    public static CourierException orderAlreadyAssigned(Long courierId, Long orderId) {
        return new CourierException("Order " + orderId + " is already assigned to courier " + courierId);
    }


    public static CourierException orderDoesNotExist(Long courierId, Long orderId) {
        return new CourierException("Order " + orderId + " wasn't previously assigned to courier " + courierId);
    }
}
