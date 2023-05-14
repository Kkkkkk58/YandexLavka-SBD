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
@DiscriminatorValue(OrderStatus.Values.ASSIGNED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignedOrderState extends OrderState {

    public AssignedOrderState(Order order, LocalDateTime timestamp, Courier courier) {
        super(OrderStatus.ASSIGNED, timestamp, order, courier);
    }

    @Override
    public void proceed(LocalDateTime timestamp, Courier courier) {

        validateTimestamp(timestamp);
        if (!courier.equals(getCourier())) {
            throw new RuntimeException("//TODO");
        }

        Order order = getOrder();
        OrderState newState = new DeliveredOrderState(order, timestamp, courier);
        order.setState(newState);
    }

    @Override
    public void rollback(LocalDateTime timestamp) {

        validateTimestamp(timestamp);

        Order order = getOrder();
        OrderState newState = new CreatedOrderState(getOrder(), timestamp);
        order.setState(newState);
    }

}
