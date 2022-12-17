package io.github.eeroom.javacore.ftp;

public enum FtpTransferMode {
    续传(0),
    覆盖(1),
    已存在则忽略(2),
    已存在则异常(3);
    int mode;
    FtpTransferMode(int mode){
        this.mode=mode;
    }
}
