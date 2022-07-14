package io.github.eeroom.javacore.thread;

/**
 * 1.5版本以前：
 */
public class App {
    public static void  main(String[] args){
        callSynchronized();
    }

    /**
     * 场景：电子黑板报循环展示学生信息，有2个学生，{张三,10岁}，{李四,12岁}
     * 一个线程负责更新信息
     * 一个线程负责展示信息
     */
    private static void callSynchronized() {
        var student=new Student();
        Thread t1=new Thread(()->{
            boolean flag=false;
            while (true){
                flag=!flag;
                if(flag){
                    student.age=10;
                    student.name="张三";
                }else {
                    student.age=12;
                    student.name="李四";
                }
            }
        });
        Thread t2=new Thread(()->{
            while (true){
                System.out.printf("姓名：%s,年龄：%d\r\n",student.name,student.age);
            }
        });
        t1.start();
        t2.start();

    }
}

