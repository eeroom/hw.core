package io.github.eeroom.jni;

import com.sun.jna.win32.StdCallLibrary;

public interface IAcHandlerCallback extends StdCallLibrary.StdCallCallback {
    int Add(int p1,int p2);
}
