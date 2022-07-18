package io.github.eeroom.javacore.thread;

import java.util.HashMap;

/**
 * 1.5版本以前：
 */
public class App {
    public static void main(String[] args) {
        var dict=new HashMap<Integer,Runnable>();
        dict.put(0,App::showStudentInfo);
        dict.put(1,App::showStudentInfoSynchronized);
        dict.put(2,App::showStudentInfoSynchronizedV2);
        dict.put(3,App::showStudentInfoSynchronizedV3);
        dict.put(4,App::BreadDemo);
        dict.get(4).run();
    }


    /**
     * 场景：面包店，厨师做面包，顾客买面包
     * 一个或多个线程模拟厨师做面包，做好的面包放在指定位置等待用户购买，为了顾客买到最新出炉的面包，指定位置只能放一个面包，厨师等这个面包卖掉后再做下一个面包
     * 一个或多个线程模拟顾客买面包，只买最新做好的面包，如果没有最新做好的面包，就等待厨师把面包做好
     */
    private static void BreadDemo() {
        var bread=new Bread();
        Thread t1 = new Thread(new Producer(bread));
        Thread t2 = new Thread(new Producer(bread));
        Thread t3 = new Thread(new Consumer(bread));
        Thread t4 = new Thread(new Consumer(bread));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    private static void showStudentInfoSynchronizedV3() {
        var student = new Student();
        Thread t1 = new Thread(new SetStudentHandlerSynchronizedV3(student));
        Thread t2 = new Thread(new ShowStudentHandlerSynchronizedV3(student));
        t1.start();
        t2.start();
    }

    private static void showStudentInfoSynchronizedV2() {
        var student = new Student();
        Thread t1 = new Thread(new SetStudentHandlerSynchronizedV2(student));
        Thread t2 = new Thread(new ShowStudentHandlerSynchronizedV2(student));
        t1.start();
        t2.start();
    }

    private static void showStudentInfoSynchronized() {
        var student = new Student();
        Thread t1 = new Thread(new SetStudentHandlerSynchronized(student));
        Thread t2 = new Thread(new ShowStudentHandlerSynchronized(student));
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
    private static void showStudentInfo() {
        var student = new Student();
        Thread t1 = new Thread(new SetStudentHandler(student));
        Thread t2 = new Thread(new ShowStudentHandler(student));
        t1.start();
        t2.start();
    }
}

