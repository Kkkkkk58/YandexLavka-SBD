package ru.yandex.yandexlavka;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.yandex.yandexlavka")
@OpenAPIDefinition(info = @Info(title = "Yandex Lavka", version = "1.0"))
@EnableJpaRepositories(value = "ru.yandex.yandexlavka.dataaccess.repositories")
@EntityScan("ru.yandex.yandexlavka.dataaccess.entities")
public class YandexLavkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YandexLavkaApplication.class, args);
    }

}
