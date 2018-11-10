package com.tjohnn.baking.util;

public class DataWrapper<T> {

    private final T data;
    private final Throwable throwable;
    private final boolean hasException;

    public DataWrapper(T data, Throwable throwable, boolean hasException) {
        this.data = data;
        this.throwable = throwable;
        this.hasException = hasException;
    }

    public T getData() {
        return data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean getHasException() {
        return hasException;
    }
}
