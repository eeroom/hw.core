package io.github.eeroom.springboot1x.pojo;

public class Student {
    int id;
    int age;

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

    String name;

    public Student getChirld() {
        return chirld;
    }

    public void setChirld(Student chirld) {
        this.chirld = chirld;
    }

    Student chirld;

}
