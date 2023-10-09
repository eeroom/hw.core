package io.github.eeroom.javacore.定时器;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutor固定频率 implements Runnable {
    public static void main(String[] args) throws Throwable {
        var scheduledThreadPoolExecutor=new java.util.concurrent.ScheduledThreadPoolExecutor(4);
        /**
         * 定时器周期为3s
         * 定时器以同步方式执行任务，如果任务卡住，则原本的下一次定时器周期并不会执行
         * 如果任务的执行时间小于定时器周期，则每3s执行一次
         * 如果任务的执行时间大于定时器周期，则任务结束，定时器会立刻启动下一次执行
         */
       scheduledThreadPoolExecutor.scheduleAtFixedRate(new ScheduledThreadPoolExecutor固定频率(),0, 3,TimeUnit.SECONDS);
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
