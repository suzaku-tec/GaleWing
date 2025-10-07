package com.galewings.util.stream;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;
}
