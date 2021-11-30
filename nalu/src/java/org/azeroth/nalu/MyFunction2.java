package org.azeroth.nalu;

@FunctionalInterface
public interface MyFunction2<T,B,C> {
    public C apply(T t,B b);
}
