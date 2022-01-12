package io.github.eeroom.jni;

import com.sun.jna.Library;

public interface IAclib extends Library {
    final String dllName="aclib";
    int Add(int p1,int p2);
    int Handler(String p1,IAcHandlerCallback iAcHandlerCallback);
}
