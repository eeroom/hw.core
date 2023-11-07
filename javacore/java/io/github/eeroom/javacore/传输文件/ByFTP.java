package io.github.eeroom.javacore.传输文件;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  ftp操作流程：登陆，登陆成功后进入用户的默认目录，切工作目录，上传或者下载文件
 */
public class ByFTP {

    public static void main(String[] args) throws Throwable{
        listFiles();
//        upload();
//        download();
    }

    static void listFiles() throws Throwable {
        var ftpaddr="ftp://192.168.1.2";
        var remoteFileFullPath="nuget";

        var dictFtpfile=new HashMap<String, ArrayList<FTPFile>>();
        var lstDir=new LinkedList<String>();
        lstDir.add(remoteFileFullPath);
        String dirPath=null;
        var ftpClient= ByFTP.loginAndSoupportZh(ftpaddr,"Deroom","BT151");
        while ((dirPath=lstDir.poll())!=null){
            ftpClient.disconnect();
            ByFTP.loginAndSoupportZh(ftpaddr,"Deroom","BT151",ftpClient);
            ByFTP.changeWorkDirectory(ftpClient,dirPath);
            var lst=ftpClient.listFiles();
            var lstFile=new ArrayList<FTPFile>();
            for(var file:lst){
                if(file.isFile())
                    lstFile.add(file);
                else if(file.isDirectory()){
                    var fileName=file.getName();
                    if(".".equals(fileName) || "..".equals(fileName))
                        continue;
                    lstDir.add(dirPath+"/"+file.getName());
                }
            }
            if(lstFile.size()>0)
                dictFtpfile.put(dirPath,lstFile);
        }
        ftpClient.disconnect();
        for(var dir:dictFtpfile.keySet()){
            for(var file:dictFtpfile.get(dir)){
                System.out.format("目录：%s,文件名称：%s\r\n",dir,file.getName());
            }
        }
    }

    static void upload() throws Throwable{
        var localfileFullPath="D:\\QQ影音(v1.2.5).ipa";
        var ftpaddr="ftp://192.168.1.2";
        var remoteFileFullPath="nuget/dir1/dir2/QQ影音(v1.2.5).ipa";
        FTPClient ftpClient=null;
        try{
            ftpClient= ByFTP.loginAndSoupportZh(ftpaddr,"Deroom","BT151");
            var remoteFile=new File(remoteFileFullPath);
            ByFTP.changeWorkDirectory(ftpClient,remoteFile.getParent());
            ByFTP.upload(ftpClient,localfileFullPath,remoteFile.getName(), TransferRule.已存在则忽略);
        }finally {
            if (ftpClient!=null && ftpClient.isConnected())
                ftpClient.disconnect();
        }
    }

    static void download() throws Throwable{
        var localfileFullPath="D:\\QQ影音(v1.2.5).ipa222";
        var ftpaddr="ftp://192.168.1.2:21";
        var remoteFileFullPath="nuget/dir1/dir2/QQ影音(v1.2.5).ipa";
        FTPClient ftpClient=null;
        try{
            ftpClient= ByFTP.loginAndSoupportZh(ftpaddr,"Deroom","BT151");
            var remoteFile=new File(remoteFileFullPath);
            ByFTP.changeWorkDirectory(ftpClient,remoteFile.getParent());
            ByFTP.download(ftpClient,localfileFullPath,remoteFile.getName(), TransferRule.已存在则忽略);
        }finally {
            if (ftpClient!=null && ftpClient.isConnected())
                ftpClient.disconnect();
        }
    }

    /**
     *
     * @param ftpaddr eg. ftp://a.b.c:21
     * @param userName
     * @param pwd
     * @return
     * @throws Throwable
     */
    public static FTPClient loginAndSoupportZh(String ftpaddr,String userName,String pwd) throws Throwable {
        return loginAndSoupportZh(ftpaddr,userName,pwd,new FTPClient());
    }

    public static FTPClient loginAndSoupportZh(String ftpaddr,String userName,String pwd,FTPClient ftpClient) throws Throwable {
        ftpClient.setDataTimeout(15*1000);//超时时间为10s
        ftpClient.setBufferSize(2048*1024);
        ftpClient.setSendBufferSize(2048*1024);
        URI targetFtp=URI.create(ftpaddr);
        var port=targetFtp.getPort();
        if(port<0)
            port=21;
        ftpClient.connect(targetFtp.getHost(),port);
        if(!ftpClient.login(userName,pwd)){
            throw new RuntimeException("login faild;replycode:"+ftpClient.getReplyCode());
        }
        //ftp协议中，文件名、文件路径的编码采用ISO_8859_1，FTP.DEFAULT_CONTROL_ENCODING
        //中文路径和文件的处理，前提：服务器支持字符集GBK或者UTF-8等
        //客户端保持和服务端一致的字符集，我们假定服务端为GBK，这样即使服务端实际不支持GBK，对于英文的路径和文件名仍然能够能正常处理
        var serverCharsetName="GBK";
        var utf8Enable=ftpClient.sendCommand("OPTS UTF8","ON");
        if(FTPReply.isPositiveCompletion(utf8Enable)){
            serverCharsetName="UTF-8";
        }
        //FTP.DEFAULT_CONTROL_ENCODING
        ftpClient.setControlEncoding(serverCharsetName);
        if(!ftpClient.setFileType(FTP.BINARY_FILE_TYPE))
            throw new RuntimeException("setFileType faild;replycode:"+ftpClient.getReplyCode());
        return ftpClient;
    }

    /**
     * 切到目标目录
     * @param ftpClient
     * @param remoteDirectory 空字符串或者null值表示不切，/或者/开头表示首先切到ftp服务的根目录，不以/开头表示从当前目录开始往里切
     * @throws Throwable
     */
    public static void changeWorkDirectory(FTPClient ftpClient, String remoteDirectory)  throws Throwable{
        if(remoteDirectory==null || remoteDirectory.equals(""))
            return;
        remoteDirectory=remoteDirectory.replaceAll("\\\\","/").trim();
        if(remoteDirectory.startsWith("/")){
            if(!ftpClient.changeWorkingDirectory("/"))
                throw new RuntimeException("changeWorkingDirectory faild;replycode:"+ftpClient.getReplyCode());
        }
        if(remoteDirectory.equals("/"))
            return;
        var buffer=remoteDirectory.getBytes(ftpClient.getControlEncoding());
        String remoteDirectoryBy8859=new String(buffer, FTP.DEFAULT_CONTROL_ENCODING);
        var lstpathsengment=remoteDirectoryBy8859.split("/");
        for (var sengment:lstpathsengment){
            if(sengment.equals(""))
                continue;
            if(ftpClient.changeWorkingDirectory(sengment))
                continue;
            if(!ftpClient.makeDirectory(sengment))
                throw new RuntimeException("makeDirectory faild;replycode:"+ftpClient.getReplyCode());
            if(!ftpClient.changeWorkingDirectory(sengment))
                throw new RuntimeException("changeWorkingDirectory faild;replycode:"+ftpClient.getReplyCode());
        }
    }

    public static void upload(FTPClient ftpClient, String localfileFullPath, String remoteFileName, TransferRule mode) throws Throwable {
        var remoteFileNameBy8859=new String(remoteFileName.getBytes(ftpClient.getControlEncoding()),FTP.DEFAULT_CONTROL_ENCODING);
        var lstfile= Arrays.stream(ftpClient.listFiles()).filter(x->x.getName().equals(remoteFileName)).collect(Collectors.toList()).toArray(FTPFile[]::new);
        if(lstfile.length>0){
            if(lstfile[0].isDirectory())
                throw new RuntimeException("服务器目录中已经存在同名的文件夹:"+remoteFileName);
            if(mode== TransferRule.已存在则异常)
                throw new RuntimeException("服务器目录中已经存在同名的文件:"+remoteFileName);
            else if(mode== TransferRule.已存在则忽略)
                return;
        }
        try (var raf=new RandomAccessFile(localfileFullPath,"r");var fs=new FileInputStream(raf.getFD())){
            if(mode== TransferRule.续传 && lstfile.length>0){
                var offset=lstfile[0].getSize();
                raf.seek(offset);
                ftpClient.setRestartOffset(offset);
            }
            try {
                ftpClient.enterLocalPassiveMode();//被动模式，21控制，
                if(!ftpClient.storeFile(remoteFileNameBy8859,fs))
                    throw new RuntimeException("storeFile faild;replycode:"+ftpClient.getReplyCode());
            }catch (Throwable throwable){
                ftpClient.enterLocalActiveMode();
                if(!ftpClient.storeFile(remoteFileNameBy8859,fs))
                    throw new RuntimeException("storeFile faild;replycode:"+ftpClient.getReplyCode(),throwable);
            }
        }
    }

    public static  void download(FTPClient ftpClient, String localfileFullPath, String remoteFileName, TransferRule mode) throws Throwable{
        var remoteFileNameBy8859=new String(remoteFileName.getBytes(ftpClient.getControlEncoding()),FTP.DEFAULT_CONTROL_ENCODING);
        var lstremoteFile= ftpClient.listFiles(remoteFileNameBy8859);
        if(lstremoteFile.length<1)
            throw new RuntimeException("服务器目录中不存在指定的文件:"+remoteFileName);
        var file=new File(localfileFullPath);
        if(file.exists()){
            if(file.isDirectory())
                throw new RuntimeException("本地磁盘已经存在同名的文件夹:"+remoteFileName);
            if(mode== TransferRule.已存在则异常)
                throw new RuntimeException("本地磁盘已经存在同名的文件:"+remoteFileName);
            else if(mode== TransferRule.已存在则忽略)
                return;
        }else{
            file.createNewFile();
        }
        try (var raf=new RandomAccessFile(file,"rw");var fs=new FileOutputStream(raf.getFD())){
            if(mode== TransferRule.续传){
                var offset=file.length();
                raf.seek(offset);
                ftpClient.setRestartOffset(offset);
            }
            try {
                ftpClient.enterLocalPassiveMode();//被动模式，21控制，
                if(!ftpClient.retrieveFile(remoteFileNameBy8859,fs))
                    throw new RuntimeException("retrieveFile faild;replycode:"+ftpClient.getReplyCode());
            }catch (Throwable throwable){
                ftpClient.enterLocalActiveMode();
                if(!ftpClient.retrieveFile(remoteFileNameBy8859,fs))
                    throw new RuntimeException("retrieveFile faild;replycode:"+ftpClient.getReplyCode(),throwable);
            }
        }
    }
}
