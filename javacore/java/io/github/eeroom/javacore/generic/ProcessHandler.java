package io.github.eeroom.javacore.generic;

public class ProcessHandler<T> {

    T entity;

    public void SetTarget(T target){
        this.entity=target;
    }

    public  T get(int index){
        return this.entity;
    }
}
