package io.github.eeroom.tcpproxy;

import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;

import java.util.Scanner;

public class App {
    public static void main(String[] arg) throws Throwable{
        var scanner=new Scanner(System.in);
        while (true){
            System.out.println("请输入新通道的代理端口");
            var port=scanner.nextInt();
//            System.out.println("请输入目标机的ip地址");
//            var remoteIp=scanner.next();
//            System.out.println("请输入目标机的用户名");
//            var remoteUserName=scanner.next();
            openTcpChannel(port,"192.168.56.102",3389,"Administrator","wch");
            scanner.next();
        }

    }

    private static void openTcpChannel(int port,String remoteIp,int remotePort,String remoteUserName,String validFlag) throws Throwable{
        String localIp="0.0.0.0";
        var boot=new io.netty.bootstrap.ServerBootstrap();
        var groupParent=io.netty.channel.epoll.Epoll.isAvailable()?new io.netty.channel.epoll.EpollEventLoopGroup():
                new io.netty.channel.nio.NioEventLoopGroup();
        var groupChildren=io.netty.channel.epoll.Epoll.isAvailable()?new io.netty.channel.epoll.EpollEventLoopGroup():
                new io.netty.channel.nio.NioEventLoopGroup();
        boot.group(groupParent,groupChildren);
        boot.channel(io.netty.channel.epoll.Epoll.isAvailable()?io.netty.channel.epoll.EpollServerSocketChannel.class:
                io.netty.channel.socket.nio.NioServerSocketChannel.class);
        boot.option(ChannelOption.SO_REUSEADDR,true);
        boot.option(ChannelOption.SO_BACKLOG,2*1024);
        boot.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1*1000);
        boot.childOption(ChannelOption.SO_KEEPALIVE,true);
        boot.childOption(ChannelOption.TCP_NODELAY,true);
        boot.childOption(ChannelOption.SO_RCVBUF,64*1024);
        boot.childOption(ChannelOption.SO_SNDBUF,64*1024);
        boot.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(64*1024,128*1024));
        boot.childHandler(new TcpLocalChannelInitializer(remoteIp,remoteUserName,validFlag,remotePort));
        var channelTask=boot.bind(localIp,port).syncUninterruptibly();
        if(!channelTask.isSuccess())
            throw new RuntimeException("代理监听启动失败");
        System.out.printf("打开了新的通道，地址：%s;端口：%d\r\n",localIp,port);
    }
}
