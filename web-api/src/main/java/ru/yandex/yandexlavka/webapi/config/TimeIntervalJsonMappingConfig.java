package ru.yandex.yandexlavka.webapi.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.core.util.Json;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.jackson.JsonComponent;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class TimeIntervalJsonMappingConfig {

    @PostConstruct
    public void objectMapper() {

        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTimeInterval.class, new TimeIntervalJsonSerializer())
                .addDeserializer(LocalTimeInterval.class, new TimeIntervalJsonDeserializer());

        Json.mapper().registerModule(module);
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static class TimeIntervalJsonSerializer extends JsonSerializer<LocalTimeInterval> {

        @Override
        public void serialize(
                LocalTimeInterval value,
                JsonGenerator gen,
                SerializerProvider serializers) throws IOException {

            gen.writeRaw(getTimeIntervalToString(value));
        }

        private String getTimeIntervalToString(LocalTimeInterval timeInterval) {

            return "\"" + timeInterval.getIntervalBegin().format(dateTimeFormatter) + "-" + timeInterval.getIntervalEnd().format(dateTimeFormatter) + "\"";
        }
    }

    public static class TimeIntervalJsonDeserializer extends JsonDeserializer<LocalTimeInterval> {
        @Override
        public LocalTimeInterval deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {

            String[] timeIntervalString = p.getText().split("-");
            LocalTime start = LocalTime.parse(timeIntervalString[0], dateTimeFormatter);
            LocalTime end = LocalTime.parse(timeIntervalString[1], dateTimeFormatter);

            return new LocalTimeInterval(start, end);
        }
    }
}
