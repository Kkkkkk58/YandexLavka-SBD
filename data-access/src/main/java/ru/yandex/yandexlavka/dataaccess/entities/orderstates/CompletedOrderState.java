package ru.yandex.yandexlavka.dataaccess.entities.orderstates;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.dataaccess.models.OrderStatus;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(OrderStatus.Values.COMPLETED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompletedOrderState extends OrderState {

    public CompletedOrderState(Order order, LocalDateTime timestamp, Courier courier) {
        super(OrderStatus.COMPLETED, timestamp, order, courier);
    }

    @Override
    public void proceed(LocalDateTime timestamp, Courier courier) {

    }
}
