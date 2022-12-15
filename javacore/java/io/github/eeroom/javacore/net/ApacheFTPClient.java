package io.github.eeroom.javacore.net;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class ApacheFTPClient {

    /**
     *
     * @param localfile eg. d:/downloads/奇幻森林[高清].rmvb
     * @param addressAndPort eg. ftp://a.b.c:21 必须包含端口
     * @param remotePath eg. /我的文件/202106/奇幻森林.rmvb
     * @param userName
     * @param pwd
     * @throws Throwable
     */
    public static void upload(String localfile, String addressAndPort,String remotePath, String userName, String pwd) throws  Throwable{
        FTPClient ftpClient=new FTPClient();
        ftpClient.setDataTimeout(15*1000);//超时时间为10s
        ftpClient.setBufferSize(2048*1024);
        ftpClient.setSendBufferSize(2048*1024);
        try {
            uploadInternal(ftpClient,localfile,addressAndPort,remotePath,userName,pwd);
        }finally {
            if(ftpClient.isConnected())
                ftpClient.disconnect();
        }
    }

    private static void uploadInternal(FTPClient ftpClient,String localfile, String addressAndPort, String remotePath, String userName, String pwd) throws  Throwable {
        URI targetFtp=URI.create(addressAndPort);
        ftpClient.connect(targetFtp.getHost(),targetFtp.getPort());
        //登陆
        ftpClient.login(userName,pwd);
        var loginCode=ftpClient.getReplyCode();
        if(!FTPReply.isPositiveCompletion(loginCode))
            throw new RuntimeException("ftp login faild");
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
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //cd到目标目录
        var remoteFile= new File(remotePath);
        var pathbuffer=remoteFile.getParent().getBytes(serverCharsetName);
        String remotePathBy8859=new String(pathbuffer, FTP.DEFAULT_CONTROL_ENCODING);
        changeWorkDirectory(ftpClient,remotePathBy8859,serverCharsetName);
        var remoteFileNameBy8859=new String(remoteFile.getName().getBytes(serverCharsetName),FTP.DEFAULT_CONTROL_ENCODING);
        try (var raf=new RandomAccessFile(localfile,"r");var fs=new FileInputStream(raf.getFD())){
            try {
                ftpClient.enterLocalPassiveMode();//被动模式，21控制，
                uploadInternal(ftpClient,fs,remoteFileNameBy8859,x->raf.seek(x));
            }catch (Throwable throwable){
                ftpClient.enterLocalActiveMode();
                uploadInternal(ftpClient,fs,remoteFileNameBy8859,x->raf.seek(x));
            }
        }
    }

    private static void changeWorkDirectory(FTPClient ftpClient, String remotePathBy8859,String serverCharsetName)  throws Throwable{
        if(remotePathBy8859==null || remotePathBy8859.equals(""))
            return;
        if(remotePathBy8859.startsWith("/")){
            if(!ftpClient.changeWorkingDirectory("/"))
                throw new RuntimeException("changeWorkingDirectory faild:/");
        }
        if(remotePathBy8859.equals("/"))
            return;
        var lstpathsengment=remotePathBy8859.split("/");
        var sb=new StringBuilder();
        for (var sengment:lstpathsengment){
            if(sengment.equals(""))
                continue;
            sb.append(sengment);
            sb.append("/");
            if(ftpClient.changeWorkingDirectory(sb.toString()))
                continue;
            if(!ftpClient.makeDirectory(sengment))
                throw  new RuntimeException("makeDirectory faild:"+new String(sengment.getBytes(FTP.DEFAULT_CONTROL_ENCODING),serverCharsetName));
            if(!ftpClient.changeWorkingDirectory(sb.toString()))
                throw new RuntimeException("changeWorkingDirectory faild:"+new String(sb.toString().getBytes(FTP.DEFAULT_CONTROL_ENCODING),serverCharsetName));
        }
    }

    static void  uploadInternal(FTPClient ftpClient, InputStream localfile, String remoteFileNameBy8859, Action<Long> breakpointResumeHandler) throws  Throwable{
        if(breakpointResumeHandler!=null){
            var lstRemotefile= ftpClient.listFiles(remoteFileNameBy8859);
            if(lstRemotefile.length>0)
                ftpClient.setRestartOffset(lstRemotefile[0].getSize());
            breakpointResumeHandler.accept(ftpClient.getRestartOffset());
        }
        if(!ftpClient.storeFile(remoteFileNameBy8859,localfile))
            throw  new RuntimeException("upload file faild");
    }
}
