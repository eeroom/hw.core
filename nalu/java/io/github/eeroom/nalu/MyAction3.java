package io.github.eeroom.nalu;

@FunctionalInterface
public interface MyAction3<T,B,C> {
    public void execute(T p1,B p2,C p3);
}
