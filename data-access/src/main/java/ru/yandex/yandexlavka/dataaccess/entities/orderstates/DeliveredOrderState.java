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
@DiscriminatorValue(OrderStatus.Values.DELIVERED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveredOrderState extends OrderState {

    public DeliveredOrderState(Order order, LocalDateTime timestamp, Courier courier) {
        super(OrderStatus.DELIVERED, timestamp, order, courier);
    }

    @Override
    public void proceed(LocalDateTime timestamp, Courier courier) {
        throw new RuntimeException("//TODO");
    }

    @Override
    public void rollback(LocalDateTime timestamp) {

        validateTimestamp(timestamp);
        Order order = getOrder();
        OrderState newState = new AssignedOrderState(order, timestamp, getCourier());
        order.setState(newState);
    }
}
