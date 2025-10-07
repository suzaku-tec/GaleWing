package com.galewings.util.stream;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;
}