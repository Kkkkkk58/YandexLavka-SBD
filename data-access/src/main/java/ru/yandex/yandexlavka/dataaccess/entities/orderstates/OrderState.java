package ru.yandex.yandexlavka.dataaccess.entities.orderstates;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.dataaccess.exceptions.OrderStateException;
import ru.yandex.yandexlavka.dataaccess.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "order_states")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "status", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public abstract class OrderState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "status", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @OneToOne(mappedBy = "state", optional = false)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    @ToString.Exclude
    private Courier courier;

    protected OrderState(OrderStatus status, LocalDateTime timestamp, Order order, Courier courier) {
        this.status = status;
        this.timestamp = timestamp;
        this.order = order;
        this.courier = courier;
    }

    public abstract void proceed(LocalDateTime timestamp, Courier courier);

    protected void validateTimestamp(LocalDateTime timestamp) {
        if (getTimestamp().isAfter(timestamp)) {
            throw OrderStateException.updateTimestampIsAfterCurrent(getTimestamp(), timestamp);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderState that = (OrderState) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
