package ru.yandex.yandexlavka.webapi.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class TimeIntervalJsonMappingConfig {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static class TimeIntervalJsonSerializer extends JsonSerializer<TimeInterval> {

        @Override
        public void serialize(
                TimeInterval value,
                JsonGenerator gen,
                SerializerProvider serializers) throws IOException {

            gen.writeString(getTimeIntervalToString(value));
        }

        private String getTimeIntervalToString(TimeInterval timeInterval) {

            return timeInterval.getIntervalBegin().format(dateTimeFormatter) + "-" + timeInterval.getIntervalEnd().format(dateTimeFormatter);
        }
    }

    public static class TimeIntervalJsonDeserializer extends JsonDeserializer<TimeInterval> {
        @Override
        public TimeInterval deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {

            String[] timeIntervalString = p.getText().split("-");
            LocalTime start = LocalTime.parse(timeIntervalString[0], dateTimeFormatter);
            LocalTime end = LocalTime.parse(timeIntervalString[1], dateTimeFormatter);

            return new TimeInterval(start, end);
        }
    }
}
