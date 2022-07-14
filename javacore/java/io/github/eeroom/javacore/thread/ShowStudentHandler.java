package io.github.eeroom.javacore.thread;

public class ShowStudentHandler implements Runnable {
    Student student;
    public ShowStudentHandler(Student student){
        this.student=student;
    }
    @Override
    public void run() {
        while (true){
            System.out.printf("姓名：%s,年龄：%d \r\n",this.student.name,this.student.age);
        }
    }
}
