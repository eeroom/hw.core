package org.azeroth.nalu;

@FunctionalInterface
public interface MyFunction2Throwable <T,B,C> {
    C apply(T t,B b) throws  Throwable;
}
