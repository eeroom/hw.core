package io.github.eeroom.javacore.本地代码调用;

import com.sun.jna.win32.StdCallLibrary;

public interface IAcHandlerCallback extends StdCallLibrary.StdCallCallback {
    int Add(int p1,int p2);
}
