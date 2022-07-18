package io.github.eeroom.javacore.thread;

public class SendStudentHandler implements Runnable {
    Student student;
    public SendStudentHandler(Student student){
        this.student=student;
    }
    @Override
    public void run() {
        while (true){
            synchronized (this.student){
                while (this.student.flag){
                    try {
                        this.student.wait();
                    } catch (InterruptedException e) {
                        System.out.printf("PushStudentHandler-InterruptedException:"+e.getMessage());
                        return;
                    }
                }
                this.student.index=this.student.index+1;
                System.out.printf("顾客:进店：%d \r\n",this.student.index);
                this.student.flag=true;
                this.student.notify();

            }

        }


    }
}
