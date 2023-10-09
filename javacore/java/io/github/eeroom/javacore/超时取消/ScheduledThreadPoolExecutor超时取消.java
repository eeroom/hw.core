package io.github.eeroom.javacore.超时取消;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutor超时取消 implements Runnable {
    public static void main(String[] args) throws Throwable {
        var scheduledThreadPoolExecutor = new java.util.concurrent.ScheduledThreadPoolExecutor(4);
        var future = scheduledThreadPoolExecutor.schedule(new ScheduledThreadPoolExecutor超时取消(), 0, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.schedule(() -> {
            System.out.println("取消任务");
            future.cancel(true);
        }, 3 , TimeUnit.SECONDS);
    }


    @Override
    public void run() {
        System.out.print("开始任务  ");
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        try {
            Thread.sleep(20 * 1000);
            System.out.print("结束任务  ");
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        } catch (InterruptedException e) {
            System.out.print("InterruptedException  ");
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            throw new RuntimeException(e);
        }
    }
}
