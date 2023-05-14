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
import ru.yandex.yandexlavka.service.api.CourierService;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;
import ru.yandex.yandexlavka.service.dto.courier.CourierMetaInfoDto;
import ru.yandex.yandexlavka.webapi.models.request.CreateCourierRequest;
import ru.yandex.yandexlavka.webapi.models.response.CreateCouriersResponse;
import ru.yandex.yandexlavka.webapi.models.response.GetCourierMetaInfoResponse;
import ru.yandex.yandexlavka.webapi.models.response.GetCouriersResponse;

import java.time.LocalDate;

@RestController
@RequestMapping("/couriers")
// TODO @Tag + javadoc + openapi stuff
@Validated
public class CourierController {

    private final CourierService courierService;

    @Autowired
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<GetCouriersResponse> getCouriers(
            @Positive @RequestParam(defaultValue = "1", required = false) Integer limit,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer offset) {

        Pageable pageable = PageRequest.of(offset, limit);

        GetCouriersResponse response = GetCouriersResponse.builder()
                .couriers(courierService.get(pageable))
                .limit(limit)
                .offset(offset)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreateCouriersResponse> create(@Valid @RequestBody CreateCourierRequest createCourierRequest) {

        CreateCouriersResponse response = CreateCouriersResponse.builder()
                .couriers(courierService.create(createCourierRequest.couriers()))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "{courier_id}", produces = "application/json")
    public ResponseEntity<CourierDto> getById(@Positive @PathVariable(value = "courier_id") Long courierId) {
        return ResponseEntity.ok(courierService.getById(courierId));
    }

    @GetMapping(value = "meta-info/{courier_id}", produces = "application/json")
    public ResponseEntity<GetCourierMetaInfoResponse> getMetaInfo(
            @PathVariable("courier_id") Long courierId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        CourierMetaInfoDto metaInfoDto = courierService.getRatings(courierId, startDate, endDate);
        GetCourierMetaInfoResponse response = GetCourierMetaInfoResponse.builder()
                .id(metaInfoDto.id())
                .type(metaInfoDto.type())
                .regions(metaInfoDto.regions())
                .workingHours(metaInfoDto.workingHours())
                .rating(metaInfoDto.rating())
                .earnings(metaInfoDto.earnings())
                .build();

        return ResponseEntity.ok(response);
    }
}
