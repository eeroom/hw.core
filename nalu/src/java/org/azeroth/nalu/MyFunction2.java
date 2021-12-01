package org.azeroth.nalu;

@FunctionalInterface
public interface MyFunction2<T,B,C> {
    C apply(T t,B b);
}
