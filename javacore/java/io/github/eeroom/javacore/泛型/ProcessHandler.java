package io.github.eeroom.javacore.泛型;

public class ProcessHandler<T> {

    T entity;

    public void SetTarget(T target){
        this.entity=target;
    }

    public  T get(int index){
        return this.entity;
    }
}
