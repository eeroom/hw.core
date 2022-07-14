package io.github.eeroom.javacore.thread;

public class SetStudentHandlerSynchronizedV2 implements Runnable {
    Student student;

    public SetStudentHandlerSynchronizedV2(Student student) {
        this.student = student;
    }

    @Override
    public void run() {
        boolean flag = false;
        while (true) {
            if (flag) {
                this.student.setValue("张三",10);
            } else {
                this.student.setValue("李四",12);
            }
            flag=!flag;
        }
    }
}
