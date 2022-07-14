package io.github.eeroom.javacore.thread;

public class ShowStudentHandlerSynchronized implements Runnable {
    Student student;

    public ShowStudentHandlerSynchronized(Student student) {
        this.student = student;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this.student){
                System.out.printf("姓名：%s,年龄：%d \r\n", this.student.name, this.student.age);
            }
        }
    }
}
