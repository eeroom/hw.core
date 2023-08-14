package io.github.eeroom.javacore.lambda;

public interface CsFunction3<T,B,C> {
    C apply(T parameter,B parameter2)throws Throwable;
}
