package io.github.eeroom.javacore.多线程.线程同步;

import java.io.Console;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CompletableFuture教程 {

    public static void main2(String[] args) throws Throwable {

        FutureTask<String> task=new FutureTask<>(()->{
            TimeUnit.SECONDS.sleep(5);
            return "张三";
        });

        new Thread(task).start();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        var rt= task.get();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        System.out.println("rt="+rt);
    }

    /**
     * 适用场景：等价于js的Promise.all()
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {

        FutureTask<String> task=new FutureTask<>(()->{
            TimeUnit.SECONDS.sleep(5);
            return "张三";
        });

        FutureTask<String> task2=new FutureTask<>(()->{
            TimeUnit.SECONDS.sleep(5);
            return "李四";
        });

        new Thread(task).start();
        new Thread(task2).start();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        var comTask= CompletableFuture.completedFuture(task.get()).thenCombineAsync(CompletableFuture.completedFuture(task2.get()),(v1,v2)->new String[]{v1,v2});
        var rt= comTask.get();
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        System.out.println("rt="+java.util.Arrays.toString(rt) );
    }
}
