package io.github.eeroom.springcore.mybatis独立使用4xmlconfig;

public class Student {
    int id;
    int age;
    String name;
    Student chirld;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student getChirld() {
        return chirld;
    }

    public void setChirld(Student chirld) {
        this.chirld = chirld;
    }

}
