package ru.yandex.yandexlavka.dataaccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
}
