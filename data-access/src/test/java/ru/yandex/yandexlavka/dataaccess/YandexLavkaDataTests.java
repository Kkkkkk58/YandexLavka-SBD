package ru.yandex.yandexlavka.dataaccess;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.yandex.yandexlavka.dataaccess.entities.Courier;
import ru.yandex.yandexlavka.dataaccess.entities.Order;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.dataaccess.repositories.CourierRepository;
import ru.yandex.yandexlavka.dataaccess.repositories.OrderRepository;
import ru.yandex.yandexlavka.dataaccess.repositories.OrderStateRepository;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class YandexLavkaDataTests {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;
    private final OrderStateRepository orderStateRepository;

    @Autowired
    public YandexLavkaDataTests(
            DataSource dataSource,
            JdbcTemplate jdbcTemplate,
            EntityManager entityManager,
            CourierRepository courierRepository,
            OrderRepository orderRepository,
            OrderStateRepository orderStateRepository) {

        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
        this.orderStateRepository = orderStateRepository;
    }

    @Test
    public void injectComponents_componentsAreNotNull() {
        Assertions.assertNotNull(dataSource);
        Assertions.assertNotNull(jdbcTemplate);
        Assertions.assertNotNull(entityManager);
        Assertions.assertNotNull(courierRepository);
        Assertions.assertNotNull(orderRepository);
        Assertions.assertNotNull(orderStateRepository);
    }

    @Test
    public void createCourier_courierSaved() {

        CourierType courierType = CourierType.BIKE;
        Set<Integer> regions = Set.of(1);
        Set<TimeInterval> workingHours = Set.of(new TimeInterval(LocalTime.MIN, LocalTime.MAX));

        Courier courier = Courier.builder()
                .type(courierType)
                .regions(regions)
                .workingHours(workingHours)
                .build();

        courier = courierRepository.saveAndFlush(courier);

        Assertions.assertNotNull(courier.getId());
        Assertions.assertEquals(courierType, courier.getType());
        Assertions.assertEquals(regions, courier.getRegions());
        Assertions.assertEquals(workingHours, courier.getWorkingHours());
    }

    @Test
    public void createOrder_orderSaved() {

        int regions = 1;
        int weight = 2;
        Set<TimeInterval> deliveryHours = Set.of(new TimeInterval(LocalTime.MIN, LocalTime.MAX));
        int cost = 50;

        Order order = Order.builder()
                .regions(regions)
                .weight(weight)
                .deliveryHours(deliveryHours)
                .cost(cost)
                .build();

        order = orderRepository.saveAndFlush(order);

        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(regions, order.getRegions());
        Assertions.assertEquals(weight, order.getWeight());
        Assertions.assertEquals(deliveryHours, order.getDeliveryHours());
        Assertions.assertEquals(cost, order.getCost());
    }

    @Test
    @DatabaseSetup("classpath:dbUnit/data.xml")
    public void initializeEntities_entitiesAreFound() {

        Optional<Courier> courier = courierRepository.findById(1L);
        Optional<Order> order = orderRepository.findById(1L);

        Assertions.assertTrue(courier.isPresent());
        Assertions.assertTrue(order.isPresent());
    }
}
