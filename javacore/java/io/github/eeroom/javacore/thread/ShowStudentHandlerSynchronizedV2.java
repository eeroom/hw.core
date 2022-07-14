package io.github.eeroom.javacore.thread;

public class ShowStudentHandlerSynchronizedV2 implements Runnable {
    Student student;

    public ShowStudentHandlerSynchronizedV2(Student student) {
        this.student = student;
    }

    @Override
    public void run() {
        while (true) {
            this.student.showValue();
        }
    }
}
