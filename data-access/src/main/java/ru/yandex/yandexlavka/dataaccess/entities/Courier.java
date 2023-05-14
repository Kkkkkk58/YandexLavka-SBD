package ru.yandex.yandexlavka.dataaccess.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.yandex.yandexlavka.dataaccess.entities.orderstates.OrderState;
import ru.yandex.yandexlavka.dataaccess.exceptions.CourierException;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "couriers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourierType type;

    @ElementCollection(targetClass = Integer.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "courier_regions", joinColumns = @JoinColumn(name = "courier_id"))
    @Column(name = "region", nullable = false)
    private Set<Integer> regions = new HashSet<>();

    @ElementCollection(targetClass = LocalTimeInterval.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "courier_working_hours", joinColumns = @JoinColumn(name = "courier_id"))
    private Set<LocalTimeInterval> workingHours = new HashSet<>();

    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.PROTECTED)
    @ToString.Exclude
    private Set<OrderState> associatedOrdersStates = new HashSet<>();

    @Builder
    public Courier(CourierType type, Set<Integer> regions, Set<LocalTimeInterval> workingHours) {
        this.type = type;
        this.regions = new HashSet<>(regions);
        this.workingHours = new HashSet<>(workingHours);
    }

    public Set<Integer> getRegions() {
        return Collections.unmodifiableSet(regions);
    }

    public Set<LocalTimeInterval> getWorkingHours() {
        return Collections.unmodifiableSet(workingHours);
    }

    public Set<Order> getAssociatedOrders() {
        return associatedOrdersStates.stream().map(OrderState::getOrder).collect(Collectors.toSet());
    }

    public void addRegion(Integer region) {
        if (!regions.add(region)) {
            throw CourierException.regionAlreadyCovered(id, region);
        }
    }

    public void deleteRegion(Integer region) {
        if (!regions.remove(region)) {
            throw CourierException.regionDoesNotExist(id, region);
        }
    }

    public void addWorkingHours(LocalTimeInterval workingInterval) {
        if (!workingHours.add(workingInterval)) {
            throw CourierException.workingIntervalAlreadyExists(id, workingInterval);
        }
    }

    public void deleteWorkingHours(LocalTimeInterval workingInterval) {
        if (!workingHours.remove(workingInterval)) {
            throw CourierException.workingIntervalDoesNotExist(id, workingInterval);
        }
    }

    public void assignOrder(Order order) {

        if (!associatedOrdersStates.add(order.getState())) {
            throw CourierException.orderAlreadyAssigned(id, order.getId());
        }

        order.getState().setCourier(this);
    }

    public void removeOrder(Order order) {

        if (!associatedOrdersStates.remove(order.getState())) {
            throw CourierException.orderDoesNotExist(id, order.getId());
        }

        order.getState().setCourier(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Courier courier = (Courier) o;
        return id != null && Objects.equals(id, courier.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
