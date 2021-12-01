package org.azeroth.nalu;

@FunctionalInterface
public interface MyFunction<T,B> {
    B apply(T t) throws Throwable;
}
