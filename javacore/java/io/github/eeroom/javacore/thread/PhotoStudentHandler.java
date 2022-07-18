package io.github.eeroom.javacore.thread;

public class PhotoStudentHandler implements Runnable {
    Student student;
    public PhotoStudentHandler(Student student){
        this.student=student;
    }
    @Override
    public void run() {
        while (true){
            synchronized (this.student){
                while(!this.student.flag){

                    try {
                        this.student.wait();
                    } catch (InterruptedException e) {
                        System.out.printf("PhotoStudentHandler-InterruptedException:"+e.getMessage());
                        return;
                    }
                }
                System.out.printf("服务员:接待：%d \r\n",this.student.index);
                this.student.flag=false;
                this.student.notify();
            }

        }


    }
}
