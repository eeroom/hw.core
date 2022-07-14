package io.github.eeroom.javacore.thread;

public class ShowStudentHandlerSynchronizedV3 implements Runnable {
    Student student;

    public ShowStudentHandlerSynchronizedV3(Student student) {
        this.student = student;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this.student){
                System.out.printf("姓名：%s,年龄：%d \r\n", this.student.name, this.student.age);
                //唤醒一个线程监视器下的其它线程
                this.student.notify();
                try {
                    //线程暂停执行，释放cpu执行权，释放锁
                    this.student.wait();
                } catch (InterruptedException e) {
                    System.out.printf("ShowStudentHandlerSynchronizedV3-线程被中断:%s",e.getMessage());
                    break;
                }
            }
        }
    }
}
