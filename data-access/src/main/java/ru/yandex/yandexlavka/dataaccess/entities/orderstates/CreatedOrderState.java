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
@DiscriminatorValue(OrderStatus.Values.CREATED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatedOrderState extends OrderState {

    public CreatedOrderState(Order order, LocalDateTime timestamp) {
        super(OrderStatus.CREATED, timestamp, order, null);
    }


    @Override
    public void proceed(LocalDateTime timestamp, Courier courier) {

        validateTimestamp(timestamp);
        Order order = getOrder();
        OrderState newState = new CompletedOrderState(order, timestamp, courier);
        order.setState(newState);
    }

}
