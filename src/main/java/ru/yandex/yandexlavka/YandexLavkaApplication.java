package ru.yandex.yandexlavka;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Yandex Lavka", version = "1.0"))
// TODO add tests, dockerize
public class YandexLavkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YandexLavkaApplication.class, args);
    }

}
