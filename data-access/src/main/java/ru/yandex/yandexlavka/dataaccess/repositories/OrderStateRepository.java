package ru.yandex.yandexlavka.dataaccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.orderstates.OrderState;
import ru.yandex.yandexlavka.dataaccess.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderStateRepository extends JpaRepository<OrderState, Long> {

    List<OrderState> findByTimestampBetweenAndCourierAndStatus(LocalDateTime from, LocalDateTime to, Courier courier, OrderStatus status);
}
