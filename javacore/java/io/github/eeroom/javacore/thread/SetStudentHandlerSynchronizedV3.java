package io.github.eeroom.javacore.thread;

public class SetStudentHandlerSynchronizedV3 implements Runnable {
    Student student;

    public SetStudentHandlerSynchronizedV3(Student student) {
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
                //唤醒一个线程监视器下的其它线程
                this.student.notify();
                try {
                    //线程暂停执行，释放cpu执行权，释放锁
                    this.student.wait();
                } catch (InterruptedException e) {
                    System.out.printf("SetStudentHandlerSynchronizedV3-线程被中断:%s",e.getMessage());
                    break;
                }
                flag=!flag;
            }
        }
    }
}
