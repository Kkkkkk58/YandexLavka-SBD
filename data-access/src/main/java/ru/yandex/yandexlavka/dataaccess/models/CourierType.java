package ru.yandex.yandexlavka.dataaccess.models;

import lombok.Getter;

@Getter
public enum CourierType {
    FOOT(2, 3),
    BIKE(3, 2),
    AUTO(4, 1);

    private final int salaryCoefficient;
    private final int ratingCoefficient;

    CourierType(int salaryCoefficient, int ratingCoefficient) {
        this.salaryCoefficient = salaryCoefficient;
        this.ratingCoefficient = ratingCoefficient;
    }
}
