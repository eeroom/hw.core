package io.github.eeroom.proxytcp;

import io.netty.channel.Channel;
import io.netty.handler.logging.LogLevel;

public  class TcpRemoteChannelInitializer extends io.netty.channel.ChannelInitializer<Channel>{

    Channel localChannel;

    public TcpRemoteChannelInitializer(Channel localChannel){
        this.localChannel =localChannel;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        var pipeline=channel.pipeline();
        pipeline.addLast(new io.netty.handler.logging.LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new TcpRemoteChannelHandler(this.localChannel));
    }
}
