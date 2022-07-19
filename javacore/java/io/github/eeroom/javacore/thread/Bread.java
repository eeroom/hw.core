package io.github.eeroom.javacore.thread;

public class Bread {
    int num;
    boolean selling;

    java.util.concurrent.locks.ReentrantLock lock = new java.util.concurrent.locks.ReentrantLock();
    java.util.concurrent.locks.Condition producerCD = lock.newCondition();
    java.util.concurrent.locks.Condition consumerCD = lock.newCondition();

    public void make() {
        synchronized (this) {
            while (this.selling) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.num += 1;
            System.out.printf("生产者:制做面包：%d \r\n", this.num);
            this.selling = true;
            this.notify();
        }
    }

    public void makeV2() {
        synchronized (this) {
            while (this.selling) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.num += 1;
            System.out.printf("生产者:制做面包：%d \r\n", this.num);
            this.selling = true;
            this.notifyAll();
        }
    }

    public void makeV3() {
        lock.lock();
        try {
            while (this.selling) {
                this.producerCD.await();
            }
            this.num += 1;
            System.out.printf("生产者:制做面包：%d \r\n", this.num);
            this.selling = true;
            this.consumerCD.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void payV3() {
        lock.lock();
        try {
            while (!this.selling) {
                this.consumerCD.await();
            }
            System.out.printf("消费者:购买面包：%d \r\n", this.num);
            this.selling = false;
            this.producerCD.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public synchronized void payV2() {
        while (!this.selling) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("消费者:购买面包：%d \r\n", this.num);
        this.selling = false;
        this.notifyAll();
    }

    public synchronized void pay() {
        while (!this.selling) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("消费者:购买面包：%d \r\n", this.num);
        this.selling = false;
        this.notify();
    }

}
