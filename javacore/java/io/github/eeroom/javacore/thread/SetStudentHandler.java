package io.github.eeroom.javacore.thread;

public class SetStudentHandler implements Runnable {
    Student student;

    public SetStudentHandler(Student student) {
        this.student = student;
    }

    @Override
    public void run() {
        boolean flag = false;
        while (true) {
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
