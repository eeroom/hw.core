package io.github.eeroom.javacore.多线程.线程同步;

public class Producer implements Runnable {
    Runnable handler;
    public Producer(Runnable handler){
        this.handler=handler;
    }
    @Override
    public void run() {
        while (true){
            this.handler.run();
        }
    }
}
