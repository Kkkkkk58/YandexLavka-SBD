package ru.yandex.yandexlavka.service.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.yandexlavka.dataaccess.models.embeddable.TimeInterval;
import ru.yandex.yandexlavka.service.validation.annotations.NonIntersectTimeIntervalCollection;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class NonIntersectTimeIntervalCollectionValidator implements ConstraintValidator<NonIntersectTimeIntervalCollection, Collection<TimeInterval>> {

    @Override
    public boolean isValid(Collection<TimeInterval> value, ConstraintValidatorContext context) {

        if (value.parallelStream().anyMatch(Objects::isNull)) {
            return false;
        }

        TimeIntervalComparator timeIntervalComparator = new TimeIntervalComparator();
        List<TimeInterval> sortedIntervals = value.parallelStream().sorted(timeIntervalComparator).toList();

        for (int i = 1; i < sortedIntervals.size(); ++i) {
            if (haveIntersections(sortedIntervals.get(i - 1), sortedIntervals.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean haveIntersections(TimeInterval t1, TimeInterval t2) {
        return t1.getIntervalEnd().isAfter(t2.getIntervalBegin());
    }

    private static class TimeIntervalComparator implements Comparator<TimeInterval> {

        @Override
        public int compare(TimeInterval t1, TimeInterval t2) {
            if (t1.getIntervalBegin().isBefore(t2.getIntervalBegin())) {
                return -1;
            } else if (t1.getIntervalBegin().equals(t2.getIntervalBegin())) {
                if (t1.getIntervalEnd().equals(t2.getIntervalEnd())) {
                    return 0;
                } else if (t1.getIntervalEnd().isBefore(t2.getIntervalEnd())) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        }
    }
}
