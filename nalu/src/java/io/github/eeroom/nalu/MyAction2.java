package io.github.eeroom.nalu;

@FunctionalInterface
public interface MyAction2<T,B> {
    void  execute(T t,B b);
}
