package io.github.eeroom.javacore.多线程.线程同步;

public class Student {
    String name;
    int age;
    boolean flag=false;
    Object lock = new Object();

    public void change() {
        if (flag) {
            this.name = "张三";
            this.age = 10;

        } else {
            this.name = "李四";
            this.age = 12;
        }
        flag = !flag;
    }

    public synchronized void changeV2() {
        if (this.flag) {
            this.name = "张三";
            this.age = 10;

        } else {
            this.name = "李四";
            this.age = 12;
        }
        this.flag = !this.flag;
    }

    public void changeV3() {
        synchronized (this) {
            if (this.flag) {
                this.name = "张三";
                this.age = 10;

            } else {
                this.name = "李四";
                this.age = 12;
            }
            this.flag = !this.flag;
        }
    }


    public void changeV4() {
        synchronized (this.lock) {
            if (this.flag) {
                this.name = "张三";
                this.age = 10;

            } else {
                this.name = "李四";
                this.age = 12;
            }
            this.flag = !this.flag;
            this.lock.notify();
            try {
                this.lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void show() {
        System.out.printf("姓名：%s,年龄：%d \r\n", this.name, this.age);
    }

    public synchronized void showV2() {
        System.out.printf("姓名：%s,年龄：%d \r\n", this.name, this.age);
    }

    public void showV3() {
        synchronized (this) {
            System.out.printf("姓名：%s,年龄：%d \r\n", this.name, this.age);
        }
    }

    public void showV4() {
        synchronized (this.lock) {
            System.out.printf("姓名：%s,年龄：%d \r\n", this.name, this.age);
            //唤醒线程监视器下的其它某个线程
            this.lock.notify();
            try {
                //线程暂停执行，释放cpu执行权，释放锁
                this.lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
