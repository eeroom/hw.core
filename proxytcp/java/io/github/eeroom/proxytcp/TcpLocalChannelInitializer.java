package io.github.eeroom.proxytcp;

import io.netty.channel.Channel;
import io.netty.handler.logging.LogLevel;

public class TcpLocalChannelInitializer extends io.netty.channel.ChannelInitializer<Channel> {
    String remoteUserName;
    String remoteIp;
    String validFlag;
    int remotePort;
    TcpLocalChannelInitializer(String remoteIp, String remoteUserName,String validFlag,int remotePort){
        this.remoteUserName=remoteUserName;
        this.remoteIp=remoteIp;
        this.validFlag=validFlag;
        this.remotePort=remotePort;
    }
    @Override
    protected void initChannel(Channel channel) throws Exception {
        var pipeline=channel.pipeline();
        pipeline.addLast(new io.netty.handler.logging.LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new TcpLocalChannelHandler(this.remoteIp,this.remotePort));
    }
}