package ru.yandex.yandexlavka.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.service.api.CourierService;
import ru.yandex.yandexlavka.service.api.OrderService;
import ru.yandex.yandexlavka.service.dto.courier.CourierDto;
import ru.yandex.yandexlavka.service.dto.courier.CreateCourierDto;
import ru.yandex.yandexlavka.service.dto.order.CreateOrderDto;
import ru.yandex.yandexlavka.service.dto.order.OrderDto;
import ru.yandex.yandexlavka.service.exceptions.EntityException;
import ru.yandex.yandexlavka.webapi.controllers.CourierController;
import ru.yandex.yandexlavka.webapi.controllers.OrderController;
import ru.yandex.yandexlavka.webapi.models.request.CreateCourierRequest;
import ru.yandex.yandexlavka.webapi.models.request.CreateOrderRequest;

import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {OrderController.class, CourierController.class})
@TestPropertySource(properties = {
        "resilience4j.ratelimiter.instances.basic.limitForPeriod=10",
        "resilience4j.ratelimiter.instances.basic.limitRefreshPeriod=1s",
        "resilience4j.ratelimiter.instances.basic.timeoutDuration=250ms"
})
public class YandexLavkaWebApiTests {

    private static final String API_COURIERS = "/couriers";
    private static final String API_ORDERS = "/orders";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;
    @MockBean
    private CourierService courierService;

    @Test
    public void createValidOrder_isOk() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(Set.of(
                new CreateOrderDto(1, 1, Set.of(new TimeInterval(LocalTime.MIN, LocalTime.MAX)), 1)
        ));

        mockMvc.perform(post(API_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    public void createInvalidOrder_isBadRequest() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(Set.of(
                new CreateOrderDto(0, -1, Set.of(), -3)
        ));

        mockMvc.perform(post(API_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void createValidCourier_isOk() throws Exception {
        CreateCourierRequest request = new CreateCourierRequest(Set.of(
                new CreateCourierDto(CourierType.AUTO, Set.of(1), Set.of(new TimeInterval(LocalTime.MIN, LocalTime.MAX)))
        ));

        mockMvc.perform(post(API_COURIERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    public void createInvalidCourier_isBadRequest() throws Exception {

        CreateCourierRequest request = new CreateCourierRequest(Set.of(
                new CreateCourierDto(CourierType.AUTO, Set.of(-1), Set.of(new TimeInterval(LocalTime.MIN, LocalTime.MAX)))
        ));

        mockMvc.perform(post(API_COURIERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getExistingOrder_isOkWithCorrectBody() throws Exception {
        Long orderId = 1L;
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .regions(58)
                .cost(1)
                .weight(12)
                .deliveryHours(Set.of(new TimeInterval(LocalTime.MIN, LocalTime.MAX)))
                .build();
        given(orderService.getById(orderId)).willReturn(orderDto);

        MvcResult result = mockMvc
                .perform(get(API_ORDERS + "/{id}", orderId)
                        .param("id", String.valueOf(orderId))).
                andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(orderDto)
        );
    }

    @Test
    public void getExistingCourier_isOkWithCorrectBody() throws Exception {
        Long courierId = 1L;
        CourierDto courierDto = CourierDto.builder()
                .id(courierId)
                .type(CourierType.AUTO)
                .regions(Set.of(58, 64))
                .workingHours(Set.of(new TimeInterval(LocalTime.MIN, LocalTime.MAX)))
                .build();
        given(courierService.getById(courierId)).willReturn(courierDto);

        MvcResult result = mockMvc
                .perform(get(API_COURIERS + "/{id}", courierId)
                        .param("id", String.valueOf(courierId))).
                andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(courierDto));

    }

    @Test
    public void getNonExistingEntity_isNotFound() throws Exception {

        given(courierService.getById(anyLong())).willThrow(EntityException.class);

        mockMvc.perform(get(API_COURIERS + "/{id}", 200L)
                        .param("id", String.valueOf(200L))).
                andExpect(status().isNotFound());

    }

}
