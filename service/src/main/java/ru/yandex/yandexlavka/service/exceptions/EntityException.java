package ru.yandex.yandexlavka.service.exceptions;

public class EntityException extends RuntimeException {

    private EntityException(String message) {
        super(message);
    }

    public static <T, PK> EntityException entityNotFound(Class<T> clazz, PK id) {
        return new EntityException("Entity of type " + clazz.getName() + " with id " + id + " not found");
    }
}
