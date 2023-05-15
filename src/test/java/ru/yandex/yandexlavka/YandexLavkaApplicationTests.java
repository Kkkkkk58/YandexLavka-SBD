package ru.yandex.yandexlavka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.yandexlavka.dataaccess.models.CourierType;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.service.dto.courier.CreateCourierDto;
import ru.yandex.yandexlavka.service.dto.order.CreateOrderDto;
import ru.yandex.yandexlavka.webapi.models.request.CreateCourierRequest;
import ru.yandex.yandexlavka.webapi.models.request.CreateOrderRequest;

import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class YandexLavkaApplicationTests {

    private static final String API_COURIERS = "/couriers";
    private static final String API_ORDERS = "/orders";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
    }

    @Test
    public void sendingRps_isOkThenTooManyRequests() {

        int rps = 10;
        Map<Integer, Integer> responseStatusCount = new ConcurrentHashMap<>(rps * 2);

        IntStream.rangeClosed(0, rps * 2)
                .parallel()
                .forEach(i -> {
                    try {
                        MvcResult result = mockMvc.perform(get(API_COURIERS + "/{id}", 200L)
                                        .param("id", String.valueOf(200L))).
                                andReturn();

                        int statusCode = result.getResponse().getStatus();
                        responseStatusCount.put(statusCode, responseStatusCount.getOrDefault(statusCode, 0) + 1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        assertThat(responseStatusCount.keySet().size()).isEqualTo(2);
        assertThat(responseStatusCount.keySet()).contains(HttpStatus.TOO_MANY_REQUESTS.value());
    }

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

}
