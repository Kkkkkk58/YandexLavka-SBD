package ru.yandex.yandexlavka.webapi;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("ru.yandex.yandexlavka.webapi")
@EntityScan("ru.yandex.yandexlavka.dataaccess.entities")
public class YandexLavkaWebApiTestConfig {
}
