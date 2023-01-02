package io.github.eeroom.javacore.lambda;

public interface CsFunction<T> {
    T apply() throws Throwable;
}
