package ru.yandex.yandexlavka.dataaccess.models;

public enum OrderStatus {

    CREATED(Values.CREATED),
    COMPLETED(Values.COMPLETED);

    OrderStatus(String val) {

        if (!name().equals(val)) {
            throw new RuntimeException("//TODO");
        }
    }


    public static class Values {
        public static final String CREATED = "CREATED";
        public static final String COMPLETED = "COMPLETED";
    }
}
