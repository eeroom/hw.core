package io.github.eeroom.javacore.thread;

public class DoLoopHandler implements Runnable {
    Runnable handler;
    public DoLoopHandler(Runnable handler){
        this.handler=handler;
    }
    @Override
    public void run() {
        while (true){
            this.handler.run();
        }
    }
}
