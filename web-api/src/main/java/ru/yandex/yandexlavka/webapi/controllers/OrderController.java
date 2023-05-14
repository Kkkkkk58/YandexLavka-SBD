package ru.yandex.yandexlavka.webapi.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.service.api.OrderService;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;
import ru.yandex.yandexlavka.webapi.models.request.CreateOrderRequest;
import ru.yandex.yandexlavka.webapi.pagination.LimitOffsetPageable;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
@CrossOrigin
@RateLimiter(name = "basic")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Получение списка заказов с заданной пагинацией
     *
     * @param limit  Максимальное количество заказов в выдаче. Если параметр не передан, то значение по умолчанию равно 1.
     * @param offset Количество заказов, которое нужно пропустить для отображения текущей страницы. Если параметр не передан, то значение по умолчанию равно 0.
     * @return Список заказов
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<OrderDto>> getOrders(
            @Positive @RequestParam(defaultValue = "1", required = false) Integer limit,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer offset) {

        Pageable pageable = new LimitOffsetPageable(offset, limit);

        return ResponseEntity.ok(orderService.get(pageable));
    }

    /**
     * Получение сведений о заказе по идентификатору
     *
     * @param orderId Идентфикатор заказа
     * @return Данные о заказе
     */
    @GetMapping(value = "{order_id}", produces = "application/json")
    public ResponseEntity<OrderDto> getOrder(@Positive @PathVariable(value = "order_id") Long orderId) {

        return ResponseEntity.ok(orderService.getById(orderId));
    }

    /**
     * Размещение новых заказов
     *
     * @param createOrderRequest Модель запроса для размещения заказов
     * @return Данные о созданных заказах
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<OrderDto>> createOrders(@Valid @RequestBody CreateOrderRequest createOrderRequest) {

        return ResponseEntity.ok(orderService.create(createOrderRequest.orders()));
    }

    /**
     * Исполнение заказов
     *
     * @param completeOrderRequestDto Модель запроса для исполнения заказов
     * @return Данные о выполненных заказах
     */
    @PostMapping("complete")
    public ResponseEntity<List<OrderDto>> completeOrders(@Valid @RequestBody CompleteOrderRequestDto completeOrderRequestDto) {

        return ResponseEntity.ok(orderService.completeOrders(completeOrderRequestDto));
    }
}
