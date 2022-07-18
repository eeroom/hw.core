package io.github.eeroom.javacore.thread;

public class Producer implements Runnable {
    Bread bread;
    public Producer(Bread bread){
        this.bread=bread;
    }
    @Override
    public void run() {
        while (true){
            synchronized (this.bread){
                while (this.bread.selling){
                    try {
                        this.bread.wait();
                    } catch (InterruptedException e) {
                        System.out.printf("PushStudentHandler-InterruptedException:"+e.getMessage());
                        return;
                    }
                }
                this.bread.num+=1;
                System.out.printf("生产者:制做面包：%d \r\n",this.bread.num);
                this.bread.selling=true;
                this.bread.notify();
            }

        }


    }
}
