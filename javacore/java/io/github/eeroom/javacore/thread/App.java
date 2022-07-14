package io.github.eeroom.javacore.thread;

/**
 * 1.5版本以前：
 */
public class App {
    public static void main(String[] args) {
        showStudentInfo();
    }

    /**
     * 场景：电子黑板报循环展示学生信息，有2个学生，{张三,10岁}，{李四,12岁}
     * 一个线程负责更新信息
     * 一个线程负责展示信息
     * 存在的问题：会出现{姓名：李四,年龄：10}{姓名：张三,年龄：12 }的情况
     * 原因：设置姓名和设置年龄分成了2行语句，不是一个原子操作，导致展示信息的线程读取到 设置了一般信息的数据
     */
    private static void showStudentInfo() {
        var student = new Student();
        Thread t1 = new Thread(new SetStudentHandler(student));
        Thread t2 = new Thread(new ShowStudentHandler(student));
        t1.start();
        t2.start();
    }
}

