package io.github.eeroom.javacore.net;

public enum  FtpUpMode {
    续传(0),
    覆盖(1),
    已存在则忽略(2),
    已存在则异常(3);
    int mode;
    private FtpUpMode(int mode){
        this.mode=mode;
    }
}
