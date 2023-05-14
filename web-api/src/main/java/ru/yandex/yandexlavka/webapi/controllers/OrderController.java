package ru.yandex.yandexlavka.webapi.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.service.api.OrderService;
import ru.yandex.yandexlavka.service.dto.order.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;
import ru.yandex.yandexlavka.webapi.models.request.CreateOrderRequest;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<OrderDto>> getOrders(
            @Positive @RequestParam(defaultValue = "1", required = false) Integer limit,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer offset) {

        Pageable pageable = PageRequest.of(offset, limit);

        return ResponseEntity.ok(orderService.get(pageable));
    }

    @GetMapping(value = "{order_id}", produces = "application/json")
    public ResponseEntity<OrderDto> getOrder(@Positive @PathVariable(value = "order_id") Long orderId) {

        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<OrderDto>> createOrders(@Valid @RequestBody CreateOrderRequest createOrderRequest) {

        return ResponseEntity.ok(orderService.create(createOrderRequest.orders()));
    }

    @PostMapping("complete")
    public ResponseEntity<List<OrderDto>> completeOrders(@Valid @RequestBody CompleteOrderRequestDto completeOrderRequestDto) {

        return ResponseEntity.ok(orderService.completeOrders(completeOrderRequestDto));
    }
}
