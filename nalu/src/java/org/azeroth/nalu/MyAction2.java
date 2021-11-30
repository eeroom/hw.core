package org.azeroth.nalu;

@FunctionalInterface
public interface MyAction2<T,B> {
    public void  execute(T t,B b);
}
