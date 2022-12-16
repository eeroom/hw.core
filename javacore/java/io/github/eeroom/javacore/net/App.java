package io.github.eeroom.javacore.net;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;

public class App {

    public static void main(String[] args) throws  Throwable{
        var localfileFullPath="D:\\dotnet环境变量.pdf";
        var ftpaddr="ftp://192.168.56.1:21";
        var remoteFileFullPath="我的文件/202106/dotnet环境变量2.pdf";
        FTPClient ftpClient=null;
        try{
            ftpClient=ApacheFTPClient.loginAndSoupportZh(ftpaddr,"Deroom","BT151");
            var remoteFile=new File(remoteFileFullPath);
            ApacheFTPClient.changeWorkDirectory(ftpClient,remoteFile.getParent());
            ApacheFTPClient.upload(ftpClient,localfileFullPath,remoteFile.getName(),FtpUpMode.已存在则忽略);
        }finally {
            if (ftpClient!=null && ftpClient.isConnected())
                ftpClient.disconnect();
        }
    }
}
