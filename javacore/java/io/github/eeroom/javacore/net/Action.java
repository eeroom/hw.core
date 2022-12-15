package io.github.eeroom.javacore.net;

@FunctionalInterface
public interface Action<T> {
    public void accept(T value) throws Throwable;
}
