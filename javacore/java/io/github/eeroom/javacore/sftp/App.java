package io.github.eeroom.javacore.sftp;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;

public class App {

    public static void main(String[] args) throws  Throwable{
        var localfileFullPath="D:\\dotnet环境变量.pdf";
        var ftpaddr="ftp://192.168.56.1:21";
        var remoteFileFullPath="我的文件/202106/dotnet环境变量3.pdf";
        FTPClient ftpClient=null;
        try{
            ftpClient= ApacheFTPClientUtil.loginAndSoupportZh(ftpaddr,"Deroom","BT151");
            var remoteFile=new File(remoteFileFullPath);
            ApacheFTPClientUtil.changeWorkDirectory(ftpClient,remoteFile.getParent());
            ApacheFTPClientUtil.upload(ftpClient,localfileFullPath,remoteFile.getName(), FtpTransferMode.已存在则忽略);
            ApacheFTPClientUtil.download(ftpClient,"d:/333.pdf",remoteFile.getName(),FtpTransferMode.已存在则忽略);
        }finally {
            if (ftpClient!=null && ftpClient.isConnected())
                ftpClient.disconnect();
        }
        ChannelSftp sftpClient=null;
        var sftpaddr="sftp://192.168.56.102:22";
        try {
            sftpClient=SftpClientUtil.loginAndSoupportZh(sftpaddr,"root","123456");
            var remoteFile=new File(remoteFileFullPath);
            SftpClientUtil.changeWorkDirectory(sftpClient,remoteFile.getParent());
            SftpClientUtil.upload(sftpClient,localfileFullPath,remoteFile.getName(),FtpTransferMode.已存在则忽略);
            SftpClientUtil.download(sftpClient,"d:/222.pdf",remoteFile.getName(),FtpTransferMode.已存在则忽略);

        }finally {
            if(sftpClient!=null && sftpClient.isConnected()){
                sftpClient.getSession().disconnect();
                sftpClient.disconnect();
            }
        }
    }
}
