package io.github.eeroom.javacore.thread;

public class SetStudentHandlerSynchronized implements Runnable {
    Student student;

    public SetStudentHandlerSynchronized(Student student) {
        this.student = student;
    }

    @Override
    public void run() {
        boolean flag = false;
        while (true) {
            synchronized (this.student){
                if (flag) {
                    this.student.age = 10;
                    this.student.name = "张三";
                } else {
                    this.student.age = 12;
                    this.student.name = "李四";
                }
                flag=!flag;
            }
        }
    }
}
