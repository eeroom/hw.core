package org.azeroth.nalu;

@FunctionalInterface
public interface MyAction2<T,B> {
    void  execute(T t,B b);
}
