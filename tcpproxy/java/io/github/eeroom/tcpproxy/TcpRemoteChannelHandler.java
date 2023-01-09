package io.github.eeroom.tcpproxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class TcpRemoteChannelHandler extends io.netty.channel.ChannelInboundHandlerAdapter{
    Channel localChannel;
    TcpRemoteChannelHandler(Channel localChannel){
        this.localChannel=localChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.localChannel.writeAndFlush(msg);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if(this.localChannel!=null && this.localChannel.isOpen()){
            this.localChannel.config().setAutoRead(ctx.channel().isWritable());
        }
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
        ctx.fireChannelInactive();
        this.localChannel.close();
    }
}
