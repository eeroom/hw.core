package io.github.eeroom.javacore.net;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.SftpATTRS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.Properties;

public class SftpClientUtil {

    /**
     * @param ftpaddr  eg. sftp://a.b.c:22
     * @param userName
     * @param pwd
     * @return
     * @throws Throwable
     */
    public static ChannelSftp loginAndSoupportZh(String ftpaddr, String userName, String pwd) throws Throwable {
        URI targetFtp = URI.create(ftpaddr);
        var jsch = new JSch();
        var session = jsch.getSession(userName, targetFtp.getHost(), targetFtp.getPort());
        if (pwd != null && pwd.length() > 0)
            session.setPassword(pwd);
        Properties cfg = new Properties();
        cfg.put("StrictHostKeyChecking", "no");
        cfg.put("userauth.gssapi-with-mic", "no");
        session.setConfig(cfg);
        session.setServerAliveInterval(20 * 1000);
        session.connect();
        var channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        return channel;
    }

    /**
     * 切到目标目录
     *
     * @param ftpClient
     * @param remoteDirectory 空字符串或者null值表示不切，/或者/开头表示首先切到ftp服务的根目录，不以/开头表示从当前目录开始往里切
     * @throws Throwable
     */
    public static void changeWorkDirectory(ChannelSftp ftpClient, String remoteDirectory) throws Throwable {
        if (remoteDirectory == null || remoteDirectory.equals(""))
            return;
        remoteDirectory = remoteDirectory.replaceAll("\\\\", "/").trim();
        if (remoteDirectory.startsWith("/")) {
            ftpClient.cd("/");
        }
        if (remoteDirectory.equals("/"))
            return;
        var lstpathsengment = remoteDirectory.split("/");
        for (var sengment : lstpathsengment) {
            if (sengment.equals(""))
                continue;
            try {
                ftpClient.cd(sengment);
                continue;
            } catch (Throwable throwable) {
                ftpClient.mkdir(sengment);
                ftpClient.cd(sengment);
            }
        }
    }

    public static void upload(ChannelSftp ftpClient, String localfileFullPath, String remoteFileName, FtpTransferMode mode) throws Throwable {
        SftpATTRS stat = null;
        try {
            stat = ftpClient.stat(remoteFileName);
            if (!stat.isFifo())
                throw new RuntimeException("服务器目录中已经存在同名的文件夹或者符号链接或者设备:"+remoteFileName);
            if (mode == FtpTransferMode.已存在则异常)
                throw new RuntimeException("服务器目录中已经存在同名的文件:"+remoteFileName);
            else if (mode == FtpTransferMode.已存在则忽略)
                return;
        } catch (Throwable throwable) {

        }
        try (var raf = new RandomAccessFile(localfileFullPath, "r"); var fs = new FileInputStream(raf.getFD())) {
            if (mode == FtpTransferMode.续传 && stat != null)
                ftpClient.put(fs, remoteFileName, ChannelSftp.RESUME);
            else
                ftpClient.put(fs, remoteFileName, ChannelSftp.OVERWRITE);
        }
    }

    public static void download(ChannelSftp ftpClient, String localfileFullPath, String remoteFileName, FtpTransferMode mode) throws Throwable {
        try {
            var stat = ftpClient.stat(remoteFileName);
        } catch (Throwable throwable) {
            throw new RuntimeException("服务器目录中不存在指定的文件:"+remoteFileName);
        }
        var file = new File(localfileFullPath);
        if (file.exists()) {
            if (file.isDirectory())
                throw new RuntimeException("本地磁盘已经存在同名的文件夹:"+remoteFileName);
            if (mode == FtpTransferMode.已存在则异常)
                throw new RuntimeException("本地磁盘已经存在同名的文件:"+remoteFileName);
            else if (mode == FtpTransferMode.已存在则忽略)
                return;
        } else {
            file.createNewFile();
        }
        try (var raf = new RandomAccessFile(file, "rw"); var fs = new FileOutputStream(raf.getFD())) {
            if (mode == FtpTransferMode.续传 && file.length()>0)
                ftpClient.get(remoteFileName, localfileFullPath, null, ChannelSftp.RESUME);
            else
                ftpClient.get(remoteFileName, localfileFullPath, null, ChannelSftp.OVERWRITE);
        }
    }
}
