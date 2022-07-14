package io.github.eeroom.javacore.thread;

/**
 * 1.5版本以前：
 */
public class App {
    public static void main(String[] args) {
        showStudentInfo();
        showStudentInfoSynchronized();
        showStudentInfoSynchronizedV2();
    }

    private static void showStudentInfoSynchronizedV2() {
        var student = new Student();
        Thread t1 = new Thread(()->{
            boolean flag = false;
            while (true) {
                if (flag) {
                    student.setValue("张三",10);
                } else {
                    student.setValue("李四",12);
                }
                flag=!flag;
            }
        });
        Thread t2 = new Thread(()->{
            while (true){
                student.showValue();
            }
        });
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
     *
     */
    private static void showStudentInfo() {
        var student = new Student();
        Thread t1 = new Thread(new SetStudentHandler(student));
        Thread t2 = new Thread(new ShowStudentHandler(student));
        t1.start();
        t2.start();
    }
}

