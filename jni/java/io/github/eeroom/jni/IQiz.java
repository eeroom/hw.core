package io.github.eeroom.jni;

import com.sun.jna.Pointer;


public interface IQiz {
    final String dllName="d:\03tools\7z\7z.dll";
    void CreateObject(Pointer cid, Pointer iid, Pointer qzobj);
}
