package ru.yandex.yandexlavka.dataaccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.dataaccess.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
