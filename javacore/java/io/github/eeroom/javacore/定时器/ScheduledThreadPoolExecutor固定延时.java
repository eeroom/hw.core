package io.github.eeroom.javacore.定时器;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutor固定延时 implements Runnable {
    public static void main(String[] args) throws Throwable {
        var scheduledThreadPoolExecutor=new java.util.concurrent.ScheduledThreadPoolExecutor(4);
        /**
         * 定时器周期为3s
         * 定时器以同步方式执行任务，如果任务卡住，则原本的下一次定时器周期并不会执行
         * 区别：无论任务执行的时间为多少，任务结束后总是会继续等待定时器的周期的时长后，再启动下一次执行
         */
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new ScheduledThreadPoolExecutor固定延时(),0, 3,TimeUnit.SECONDS);
    }


    @Override
    public void run() {
        System.out.print("执行任务：");
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
