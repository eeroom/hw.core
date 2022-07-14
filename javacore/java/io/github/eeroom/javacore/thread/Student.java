package io.github.eeroom.javacore.thread;

public class Student {
    String name;
    int age;

    public synchronized void setValue(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public synchronized void showValue() {
        System.out.printf("姓名：%s,年龄：%d \r\n", this.name, this.age);
    }
}
