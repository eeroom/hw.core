package io.github.eeroom.javacore.thread;

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
