package io.github.eeroom.springboot2xcore.ITCast11_解析注解;

@FunctionalInterface
public interface MyAction<T>{
    T invoke() throws Throwable;
}
