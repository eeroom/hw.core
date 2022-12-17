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
            ftpClient= ApacheFTPClientUtil.loginAndSoupportZh(ftpaddr,"Deroom","BT151");
            var remoteFile=new File(remoteFileFullPath);
            ApacheFTPClientUtil.changeWorkDirectory(ftpClient,remoteFile.getParent());
            ApacheFTPClientUtil.upload(ftpClient,localfileFullPath,remoteFile.getName(), FtpTransferMode.已存在则忽略);
            ApacheFTPClientUtil.download(ftpClient,"d:/333.pdf",remoteFile.getName(),FtpTransferMode.已存在则异常);
        }finally {
            if (ftpClient!=null && ftpClient.isConnected())
                ftpClient.disconnect();
        }
    }
}
