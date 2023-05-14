package ru.yandex.yandexlavka.webapi.pagination;


import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class LimitOffsetPageable implements Pageable {

    private final int limit;
    private final int offset;
    private final Sort sort;

    public LimitOffsetPageable(int limit, int offset, Sort sort) {
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public LimitOffsetPageable(int offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    public LimitOffsetPageable(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public @NonNull Sort getSort() {
        return sort;
    }

    @Override
    public @NonNull Pageable next() {
        return new LimitOffsetPageable(Math.toIntExact(getOffset() + getPageSize()), getPageSize(), getSort());
    }

    @Override
    public @NonNull Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    public @NonNull Pageable previous() {
        return hasPrevious() ? new LimitOffsetPageable(Math.toIntExact(getOffset() - getPageSize()), getPageSize(), getSort()) : this;
    }

    @Override
    public @NonNull Pageable first() {
        return new LimitOffsetPageable(0, getPageSize(), getSort());
    }

    @Override
    public @NonNull Pageable withPage(int pageNumber) {
        return new LimitOffsetPageable(pageNumber * getPageSize(), getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
