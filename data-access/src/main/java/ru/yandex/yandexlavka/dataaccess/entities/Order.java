package ru.yandex.yandexlavka.dataaccess.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.yandex.yandexlavka.dataaccess.entities.orderstates.CreatedOrderState;
import ru.yandex.yandexlavka.dataaccess.entities.orderstates.OrderState;
import ru.yandex.yandexlavka.dataaccess.exceptions.OrderException;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "weight", nullable = false)
    @Basic(optional = false)
    private float weight;

    @Column(name = "regions", nullable = false)
    @Basic(optional = false)
    private int regions;

    @ElementCollection(targetClass = LocalTimeInterval.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "order_delivery_hours", joinColumns = @JoinColumn(name = "order_id"))
    private Set<LocalTimeInterval> deliveryHours = new HashSet<>();

    @Column(name = "cost", nullable = false)
    @Basic(optional = false)
    private int cost;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private OrderState state;

    @Builder
    public Order(float weight, int regions, Set<LocalTimeInterval> deliveryHours, int cost) {
        this.weight = weight;
        this.regions = regions;
        this.deliveryHours = deliveryHours;
        this.cost = cost;
        this.state = new CreatedOrderState(this, LocalDateTime.now());
    }

    public Set<LocalTimeInterval> getDeliveryHours() {
        return Collections.unmodifiableSet(deliveryHours);
    }

    public void addDeliveryHours(LocalTimeInterval deliveryInterval) {
        if (!deliveryHours.add(deliveryInterval)) {
            throw OrderException.deliveryIntervalAlreadyExists(id, deliveryInterval);
        }
    }

    public void deleteDeliveryHours(LocalTimeInterval deliveryInterval) {
        if (!deliveryHours.remove(deliveryInterval)) {
            throw OrderException.deliveryIntervalDoesNotExist(id, deliveryInterval);
        }
    }

    public void proceed(LocalDateTime timestamp, Courier courier) {
        state.proceed(timestamp, courier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
