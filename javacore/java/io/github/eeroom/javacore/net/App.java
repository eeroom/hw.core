package io.github.eeroom.javacore.net;

import java.io.File;

public class App {

    public static void main(String[] args) throws  Throwable{
        var localfile=new File("D:/Downloads/系统补丁安装助理WinXPx86[2013.12](完整版：重要、可选、Net3.5).zip");
        var ftpaddr="ftp://192.168.56.1:21";
        var remotepath="/我们/test/系统补丁安装助理WinXPx86[2013.12](完整版：重要、可选、Net3.5).zip";
        //ApacheFTPClient.upload(localfile,ftpaddr,remotepath,"Deroom","BT151");
    }
}
