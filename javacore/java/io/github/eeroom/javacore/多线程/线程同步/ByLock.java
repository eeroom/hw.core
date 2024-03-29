package io.github.eeroom.javacore.多线程.线程同步;

import java.util.HashMap;

/**
 *
 */
public class ByLock {
    public static void main(String[] args) {
        var dict=new HashMap<Integer,Runnable>();
        dict.put(0, ByLock::studentDemo);
        dict.put(1, ByLock::studentDemoV2);
        dict.put(2, ByLock::studentDemoV3);
        dict.put(3, ByLock::studentDemoV4);
        dict.put(4, ByLock::breadDemo);
        dict.put(5, ByLock::breadDemoV2);
        dict.put(6, ByLock::breadDemoV3);
        dict.get(5).run();
    }

    /**
     * 使用jdk1.5的新api,lock，彻底解决唤醒监视器下所有其它线程的弊端
     */
    private static void breadDemoV3() {
        var bread=new Bread();
        Thread t1 = new Thread(new Producer(bread::makeV3));
        Thread t2 = new Thread(new Producer(bread::makeV3));
        Thread t3 = new Thread(new Consumer(bread::payV3));
        Thread t4 = new Thread(new Consumer(bread::payV3));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    /**
     * 使用notifyall解决线程死锁问题，没有线程再来唤醒别的线程，
     */
    private static void breadDemoV2() {
        var bread=new Bread();
        Thread t1 = new Thread(new Producer(bread::makeV2));
        Thread t2 = new Thread(new Producer(bread::makeV2));
        Thread t3 = new Thread(new Consumer(bread::payV2));
        Thread t4 = new Thread(new Consumer(bread::payV2));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }


    /**
     * 场景：面包店，厨师做面包，顾客买面包
     * 一个或多个线程模拟厨师做面包，做好的面包放在指定位置等待用户购买，为了顾客买到最新出炉的面包，指定位置只能放一个面包，厨师等这个面包卖掉后再做下一个面包
     * 一个或多个线程模拟顾客买面包，只买最新做好的面包，如果没有最新做好的面包，就等待厨师把面包做好
     * 问题：可能会发生死锁，所有线程都被wait，没有线程再来唤醒别的线程，
     * 解决办法：使用notifyall
     * 潜在问题：会唤醒业务逻辑上不需要唤醒的线程，科学的场景：消费者购买面包后，完全不必唤醒其它的消费者线程，只需要唤醒生产者线程，但是notifyall会唤醒所有其他的线程
     */
    private static void breadDemo() {
        var bread=new Bread();
        Thread t1 = new Thread(new Producer(bread::make));
        Thread t2 = new Thread(new Producer(bread::make));
        Thread t3 = new Thread(new Consumer(bread::pay));
        Thread t4 = new Thread(new Consumer(bread::pay));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    private static void studentDemoV4() {
        var student = new Student();
        Thread t1 = new Thread(new DoLoopHandler(student::changeV4));
        Thread t2 = new Thread(new DoLoopHandler(student::showV4));
        t1.start();
        t2.start();
    }

    private static void studentDemoV3() {
        var student = new Student();
        Thread t1 = new Thread(new DoLoopHandler(student::changeV3));
        Thread t2 = new Thread(new DoLoopHandler(student::showV3));
        t1.start();
        t2.start();
    }

    private static void studentDemoV2() {
        var student = new Student();
        Thread t1 = new Thread(new DoLoopHandler(student::changeV2));
        Thread t2 = new Thread(new DoLoopHandler(student::showV2));
        t1.start();
        t2.start();
    }

    /**
     * 场景：电子黑板报循环展示学生信息，有2个学生，{张三,10岁}，{李四,12岁}
     * 一个线程负责更新信息
     * 一个线程负责展示信息
     * 存在的问题：会出现{姓名：李四,年龄：10}{姓名：张三,年龄：12 }的情况
     * 原因：两个线程操作了同一个数据，既有读取，又有写入，写入的时候，设置姓名和设置年龄分成了2行语句，不是一个原子操作，导致展示信息的线程可能读取 设置了一半信息的数据
     * 解决办法1，使用synchronized语句，环绕设置姓名和设置年龄分成了2行语句，成为一个原子操作
     * 关键点：2个线程的synchronized语句使用相同的监视器，这个方式侧重于把线程同步的逻辑放在调用方
     * 解决办法2，使用synchronized关键字修饰方法，等价代码就是synchronized语句，监视器为this,如果是静态方法，则为所在类的类型元数据
     * 关键点：这个方式侧重于把线程同步的逻辑放在被调用方，
     * 存在的另一个问题：张三和李四的信息是乱序切换，并且可能连续多次不切换，期望情况是挨个切换
     * 解决办法：使用wait和notify方法轮流停止当前所在线程和唤醒监视器下的其它线程
     * 关键点：两个线程的线程监视器相同，使用线程监视器的wait和notify方法
     */
    private static void studentDemo() {
        var student = new Student();
        Thread t1 = new Thread(new DoLoopHandler(student::change));
        Thread t2 = new Thread(new DoLoopHandler(student::show));
        t1.start();
        t2.start();
    }
}

