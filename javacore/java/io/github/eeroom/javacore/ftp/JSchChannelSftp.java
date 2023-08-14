package io.github.eeroom.javacore.ftp;

public class JSchChannelSftp {

    public static void main(String[] args) throws Throwable{
        var localfileFullPath="D:\\dotnet环境变量.pdf";
        var ftpaddr="sftp://192.168.56.102:22";
        var remoteFileFullPath="我的文件/202106/dotnet环境变量3.pdf";
        com.jcraft.jsch.ChannelSftp sftpClient=null;
        try {
            sftpClient= JSchChannelSftp.loginAndSoupportZh(ftpaddr,"root","123456");
            var remoteFile=new java.io.File(remoteFileFullPath);
            JSchChannelSftp.changeWorkDirectory(sftpClient,remoteFile.getParent());
            JSchChannelSftp.upload(sftpClient,localfileFullPath,remoteFile.getName(),FtpTransferMode.已存在则忽略);
            JSchChannelSftp.download(sftpClient,"d:/222.pdf",remoteFile.getName(),FtpTransferMode.已存在则忽略);

        }finally {
            if(sftpClient!=null && sftpClient.isConnected()){
                sftpClient.getSession().disconnect();
                sftpClient.disconnect();
            }
        }
    }

    /**
     * @param ftpaddr  eg. sftp://a.b.c:22
     * @param userName
     * @param pwd
     * @return
     * @throws Throwable
     */
    public static com.jcraft.jsch.ChannelSftp loginAndSoupportZh(String ftpaddr, String userName, String pwd) throws Throwable {
        var targetFtp = java.net.URI.create(ftpaddr);
        var jsch = new com.jcraft.jsch.JSch();
        var session = jsch.getSession(userName, targetFtp.getHost(), targetFtp.getPort());
        if (pwd != null && pwd.length() > 0)
            session.setPassword(pwd);
        var cfg = new java.util.Properties();
        cfg.put("StrictHostKeyChecking", "no");
        cfg.put("userauth.gssapi-with-mic", "no");
        session.setConfig(cfg);
        session.setServerAliveInterval(20 * 1000);
        session.connect();
        var channel = (com.jcraft.jsch.ChannelSftp) session.openChannel("sftp");
        channel.connect();
        return channel;
    }

    /**
     * 切到目标目录
     *
     * @param ftpClient
     * @param remoteDirectory 空字符串或者null值表示不切，/或者/开头表示首先切到sftp服务的根目录，不以/开头表示从当前目录开始往里切
     * @throws Throwable
     */
    public static void changeWorkDirectory(com.jcraft.jsch.ChannelSftp ftpClient, String remoteDirectory) throws Throwable {
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

    public static void upload(com.jcraft.jsch.ChannelSftp ftpClient, String localfileFullPath, String remoteFileName, FtpTransferMode mode) throws Throwable {
        com.jcraft.jsch.SftpATTRS stat = null;
        try {
            stat = ftpClient.stat(remoteFileName);
        } catch (Throwable throwable) {
        }
        if (stat != null) {
            if (!stat.isReg())
                throw new RuntimeException("服务器目录中已经存在同名的文件夹或者符号链接或者设备:" + remoteFileName);
            if (mode == FtpTransferMode.已存在则异常)
                throw new RuntimeException("服务器目录中已经存在同名的文件:" + remoteFileName);
            else if (mode == FtpTransferMode.已存在则忽略)
                return;
        }
        try (var raf = new java.io.RandomAccessFile(localfileFullPath, "r"); var fs = new java.io.FileInputStream(raf.getFD())) {
            if (mode == FtpTransferMode.续传 && stat != null)
                ftpClient.put(fs, remoteFileName, com.jcraft.jsch.ChannelSftp.RESUME);
            else
                ftpClient.put(fs, remoteFileName, com.jcraft.jsch.ChannelSftp.OVERWRITE);
        }
    }

    public static void download(com.jcraft.jsch.ChannelSftp ftpClient, String localfileFullPath, String remoteFileName, FtpTransferMode mode) throws Throwable {
        com.jcraft.jsch.SftpATTRS stat = null;
        try {
            stat = ftpClient.stat(remoteFileName);
        } catch (Throwable throwable) {
            throw new RuntimeException("服务器目录中不存在指定的文件:" + remoteFileName);
        }
        if(!stat.isReg()){
            throw  new RuntimeException("服务器目录中的指定文件存在，但不是普通文件:"+remoteFileName);
        }
        var file = new java.io.File(localfileFullPath);
        if (file.exists()) {
            if (file.isDirectory())
                throw new RuntimeException("本地磁盘已经存在同名的文件夹:" + remoteFileName);
            if (mode == FtpTransferMode.已存在则异常)
                throw new RuntimeException("本地磁盘已经存在同名的文件:" + remoteFileName);
            else if (mode == FtpTransferMode.已存在则忽略)
                return;
        } else {
            file.createNewFile();
        }
        try (var raf = new java.io.RandomAccessFile(file, "rw"); var fs = new java.io.FileOutputStream(raf.getFD())) {
            if (mode == FtpTransferMode.续传 && file.length() > 0)
                ftpClient.get(remoteFileName, localfileFullPath, null, com.jcraft.jsch.ChannelSftp.RESUME);
            else
                ftpClient.get(remoteFileName, localfileFullPath, null, com.jcraft.jsch.ChannelSftp.OVERWRITE);
        }
    }
}
