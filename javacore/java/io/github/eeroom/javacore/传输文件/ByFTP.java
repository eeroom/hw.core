package io.github.eeroom.javacore.传输文件;

import org.apache.commons.io.input.buffer.CircularByteBuffer;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.net.URI;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ToLongBiFunction;
import java.util.stream.Collectors;

/**
 *  ftp操作流程：登陆，登陆成功后进入用户的默认目录，切工作目录，上传或者下载文件
 */
public class ByFTP {

    public static void main(String[] args) throws Throwable{
        //listFiles();
       upload();
//        download();
    }

    static void listFiles() throws Throwable {
        var ftpaddr="ftp://192.168.1.2";
        var remoteFileFullPath="nuget";

        var dictFtpfile=new HashMap<String, ArrayList<FTPFile>>();
        var lstDir=new LinkedList<String>();
        lstDir.add(remoteFileFullPath);
        String dirPath=null;
        var ftpClient= new FTPClient();
        do {
            ByFTP.loginAndSoupportZh(ftpClient,ftpaddr,"Deroom","BT151");
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
            ftpClient.disconnect();
        }while ((dirPath=lstDir.poll())!=null);

        for(var dir:dictFtpfile.keySet()){
            for(var file:dictFtpfile.get(dir)){
                System.out.format("目录：%s,文件名称：%s\r\n",dir,file.getName());
            }
        }

    }

    static void upload() throws Throwable{
        var localfileFullPath="D:\\Downloads\\CentOS-6.10-x86_64-minimal.iso";
        var ftpaddr="ftp://192.168.1.2";
        var remoteFileFullPath="nuget/dir1/dir2/QQ影音(v1.2.5).ip2a273";
        FTPClient ftpClient=new FTPClient();
        try{
            ftpClient= ByFTP.loginAndSoupportZh(ftpClient,ftpaddr,"Deroom","BT151");
            var remoteFile=new File(remoteFileFullPath);
            ByFTP.changeWorkDirectory(ftpClient,remoteFile.getParent());
            System.out.println("开始："+new SimpleDateFormat("HH:mm:ss").format(new Date()));
            ByFTP.upload(ftpClient,localfileFullPath,remoteFile.getName(), TransferRule.已存在则忽略,5*1024*1024,(total,sended)->{
                //System.out.println(MessageFormat.format("总共：{0}，已发送：{1}",total,sended));
            });
            System.out.println("结束："+new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }finally {
            if (ftpClient!=null && ftpClient.isConnected())
                ftpClient.disconnect();
        }
    }

    static void download() throws Throwable{
        var localfileFullPath="D:\\QQ影音(v1.2.5).ipa2322";
        var ftpaddr="ftp://192.168.1.2:21";
        var remoteFileFullPath="nuget/dir1/dir2/QQ影音(v1.2.5).ipa";
        FTPClient ftpClient=new FTPClient();
        try{
            ByFTP.loginAndSoupportZh(ftpClient,ftpaddr,"Deroom","BT151");
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
     * @param ftpClient
     * @param ftpaddr eg. ftp://a.b.c:21
     * @param userName
     * @param pwd
     * @return
     * @throws Throwable
     */
    public static FTPClient loginAndSoupportZh(FTPClient ftpClient,String ftpaddr,String userName,String pwd) throws Throwable {
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

    public static void upload(FTPClient ftpClient, String localfileFullPath, String remoteFileName, TransferRule transferRule, int maxspeed,BiConsumer<Long,Long> onSendingData) throws Throwable {
        var remoteFileNameBy8859=new String(remoteFileName.getBytes(ftpClient.getControlEncoding()),FTP.DEFAULT_CONTROL_ENCODING);
        var lstfile= Arrays.stream(ftpClient.listFiles()).filter(x->x.getName().equals(remoteFileName)).collect(Collectors.toList()).toArray(FTPFile[]::new);
        if(lstfile.length>0){
            if(lstfile[0].isDirectory())
                throw new RuntimeException("服务器目录中已经存在同名的文件夹:"+remoteFileName);
            if(transferRule== TransferRule.已存在则异常)
                throw new RuntimeException("服务器目录中已经存在同名的文件:"+remoteFileName);
            else if(transferRule== TransferRule.已存在则忽略)
                return;
        }
        try (var raf=new RandomAccessFile(localfileFullPath,"r");var fs=new FileInputStream(raf.getFD())){
            long offset=0;
            if(transferRule== TransferRule.续传 && lstfile.length>0){
                offset=lstfile[0].getSize();
                raf.seek(offset);
                ftpClient.setRestartOffset(offset);
            }
            try {
                ftpClient.enterLocalPassiveMode();//被动模式，21控制，
                try (var ostream= ftpClient.storeFileStream(remoteFileNameBy8859))
                {
                    int bufferSize=getBufferSize4Speed(fs,ostream,maxspeed,offset,100,3);
                    int length=0;
                    byte[] buffer=new byte[bufferSize];
                    long total=raf.length();
                    long sended=raf.getFilePointer();
                    while ((length=fs.read(buffer,0,bufferSize))>0){
                        ostream.write(buffer,0,length);
                        sended+=length;
                        onSendingData.accept(total,sended);
                    }
                }
            }catch (Throwable throwable){
                ftpClient.enterLocalActiveMode();
                try (var ostream= ftpClient.storeFileStream(remoteFileNameBy8859))
                {
                    int bufferSize=getBufferSize4Speed(fs,ostream,maxspeed,offset,100,3);
                    int length=0;
                    byte[] buffer=new byte[bufferSize];
                    long total=raf.length();
                    long sended=raf.getFilePointer();
                    while ((length=fs.read(buffer,0,bufferSize))>0){
                        ostream.write(buffer,0,length);
                        sended+=length;
                        onSendingData.accept(total,sended);
                    }
                }catch (Throwable exception){
                    throw new RuntimeException("storeFile faild;replycode:"+ftpClient.getReplyCode(),exception);
                }
            }
        }
    }

    /**
     * 原理：传输速度和bufferSize正相关，通过不断调整得到和目标速度匹配的bufferSize
     * 实际速度受多种因素影响，没有必要一直去调整bufferSize，只要一个相对匹配的值就可以
     * @param fs
     * @param ostream
     * @param targetSpeed 目标速度
     * @param offset 文件已经
     * @param caiyangSize
     * @param lingmidu
     * @return
     * @throws Throwable
     */
    static int getBufferSize4Speed(FileInputStream fs,OutputStream ostream,long targetSpeed ,long offset,int caiyangSize,int lingmidu) throws Throwable{
        int bufferSize=8192;
        int length=0;
        byte[] buffer=new byte[bufferSize];
        long sended=offset;
        int caiyangIndex=0;
        long preTime=new Date().getTime();
        long preSended=sended;
        int oktimes=0;
        long wuchaMax=50*1024;//速度最大误差 50KB/s
        while ((length=fs.read(buffer,0,bufferSize))>0){
            ostream.write(buffer,0,length);
            sended+=length;
            caiyangIndex++;
            if(caiyangIndex>caiyangSize){
                caiyangIndex=0;
                long nowTime=new Date().getTime();
                long timeSpan=nowTime-preTime;
                preTime=nowTime;
                if(timeSpan<=0)
                    timeSpan=1;
                long sendedSpan=sended-preSended;
                preSended=sended;
                long speed=1000*sendedSpan/timeSpan; //单位：字节/s
                System.out.println("当前buffer："+bufferSize);
                long speed4KB=speed/1024;
                System.out.println("当前速度："+speed4KB+"KB");
                //不可能精确，波动范围内不干预
                long chazhi= Math.abs(targetSpeed-speed);
                if(chazhi<wuchaMax){
                    oktimes++;
                    if(oktimes>lingmidu)
                        return bufferSize;
                }else {
                    oktimes--;
                    if(oktimes>0)
                        continue;
                    oktimes=0;
                    if(speed<targetSpeed)
                        bufferSize+=bufferSize/2;
                    else
                        bufferSize-=bufferSize/2;
                    if(bufferSize<1)
                        return 1;
                    if(bufferSize>8192*200)
                        return bufferSize;
                    if(bufferSize>buffer.length)
                        buffer=new byte[bufferSize];
                }
            }
        }
        return  bufferSize;
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
