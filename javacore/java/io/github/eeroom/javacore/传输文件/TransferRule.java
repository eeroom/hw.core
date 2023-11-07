package io.github.eeroom.javacore.传输文件;

public enum TransferRule {
    续传(0),
    覆盖(1),
    已存在则忽略(2),
    已存在则异常(3);
    int mode;
    TransferRule(int mode){
        this.mode=mode;
    }
}
