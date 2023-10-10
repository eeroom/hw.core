package io.github.eeroom.javacore.jni;

public class App {
    public static void main( String[] args )
    {
        //调用Azeorth.Core的Aclib项目类库，把lib放在resource目录就好，运行的时候，dll和jar在同一目录
        //dllname不要后缀，windows默认加.dll，linux默认加.so。也可以写全路径
        //64为jdk需要c类库也是按64位编译
        var iaclib=(IAclib)com.sun.jna.Native.loadLibrary(IAclib.dllName,IAclib.class);
        var rt= iaclib.Add(3,4);
        var rt2= iaclib.Handler("ha", new IAcHandlerCallback() {
            @Override
            public int Add(int p1, int p2) {
                return p1+p2;
            }
        });
        //调用win32 api,W32APIOptions.ASCII_OPTIONS这个参数很关键
        var k32= com.sun.jna.Native.loadLibrary("Kernel32", com.sun.jna.platform.win32.Kernel32.class, com.sun.jna.win32.W32APIOptions.ASCII_OPTIONS);

        System.out.println( "Hello World!" );
    }
}
