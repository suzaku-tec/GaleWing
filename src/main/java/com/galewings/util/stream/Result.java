package com.galewings.util.stream;

import java.util.function.Consumer;

public class Result<T> {
    private final T value;
    private final Exception exception;

    private Result(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(Exception exception) {
        return new Result<>(null, exception);
    }

    public static <T> Result<T> runCatching(ThrowingSupplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            return failure(e);
        }
    }

    public static Result<Void> runCatching(ThrowingRunnable runnable) {
        try {
            runnable.run();
            return success(null);
        } catch (Exception e) {
            return failure(e);
        }
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean isFailure() {
        return exception != null;
    }

    public T getOrNull() {
        return value;
    }

    public Exception getException() {
        return exception;
    }

    // 成功時の処理
    public Result<T> onSuccess(Consumer<T> action) {
        if (isSuccess()) {
            action.accept(value);
        }
        return this;
    }

    // 失敗時の処理
    public Result<T> onFailure(Consumer<Exception> action) {
        if (isFailure()) {
            action.accept(exception);
        }
        return this;
    }
}
