package ru.yandex.yandexlavka.dataaccess;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("ru.yandex.yandexlavka.dataaccess")
@EntityScan("ru.yandex.yandexlavka.dataaccess.entities")
@EnableJpaRepositories("ru.yandex.yandexlavka.dataaccess.repositories")
public class YandexLavkaDataAccessTestConfig {
}
