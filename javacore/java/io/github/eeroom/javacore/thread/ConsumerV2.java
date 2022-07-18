package io.github.eeroom.javacore.thread;

public class ConsumerV2 implements Runnable {
    Bread bread;
    public ConsumerV2(Bread bread){
        this.bread=bread;
    }
    @Override
    public void run() {
        while (true){
            synchronized (this.bread){
                while(!this.bread.selling){
                    try {
                        this.bread.wait();
                    } catch (InterruptedException e) {
                        System.out.printf("PhotoStudentHandler-InterruptedException:"+e.getMessage());
                        return;
                    }
                }
                System.out.printf("消费者:购买面包：%d \r\n",this.bread.num);
                this.bread.selling=false;
                this.bread.notifyAll();
            }
        }


    }
}