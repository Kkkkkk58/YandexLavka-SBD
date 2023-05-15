package ru.yandex.yandexlavka.dataaccess.models.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class TimeInterval {

    @Column(name = "interval_begin", nullable = false)
    private LocalTime intervalBegin;

    @Column(name = "interval_end", nullable = false)
    private LocalTime intervalEnd;
}
