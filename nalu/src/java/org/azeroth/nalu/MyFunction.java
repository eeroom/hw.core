package org.azeroth.nalu;

@FunctionalInterface
public interface MyFunction<T,B> {
    public B apply(T t) throws Throwable;
}
