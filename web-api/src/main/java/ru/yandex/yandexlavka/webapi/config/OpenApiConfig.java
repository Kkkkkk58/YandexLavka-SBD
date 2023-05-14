package ru.yandex.yandexlavka.webapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.core.jackson.ModelResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.yandexlavka.dataaccess.models.LocalTimeInterval;

@Configuration
public class OpenApiConfig {

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {

        // Anyway shows LocalTime structure instead of custom serialization :(
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTimeInterval.class, new TimeIntervalJsonMappingConfig.TimeIntervalJsonSerializer())
                .addDeserializer(LocalTimeInterval.class, new TimeIntervalJsonMappingConfig.TimeIntervalJsonDeserializer());

        return new ModelResolver(objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .registerModule(module));
    }
}

