package io.github.eeroom.javacore.lambda;

public interface CsFunction2<T,B> {
    B apply(T parameter)throws Throwable;
}
