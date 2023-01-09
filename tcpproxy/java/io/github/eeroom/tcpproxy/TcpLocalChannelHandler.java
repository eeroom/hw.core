package io.github.eeroom.tcpproxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;

public class TcpLocalChannelHandler extends io.netty.channel.ChannelInboundHandlerAdapter{

    io.netty.channel.ChannelFuture remoteChannelFuture;
    io.netty.channel.Channel remoteChannel;
    String remoteIp;
    int remotePort;
    TcpLocalChannelHandler(String remoteIp,int remotePort){
        this.remoteIp=remoteIp;
        this.remotePort=remotePort;
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        var boot=new io.netty.bootstrap.Bootstrap();
        boot.group(ctx.channel().eventLoop());
        boot.channel(io.netty.channel.epoll.Epoll.isAvailable()?io.netty.channel.epoll.EpollSocketChannel.class:
                io.netty.channel.socket.nio.NioSocketChannel.class);
        boot.option(ChannelOption.TCP_NODELAY,true);
        boot.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1*1000);
        boot.option(ChannelOption.SO_RCVBUF,64*1024);
        boot.option(ChannelOption.SO_SNDBUF,64*1024);
        boot.option(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(64*1024,128*1024));
        boot.handler(new TcpRemoteChannelInitializer(ctx.channel()));
        this.remoteChannelFuture =boot.connect(this.remoteIp,this.remotePort);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(this.remoteChannel !=null){
            this.remoteChannel.writeAndFlush(msg);
            return;
        }
        io.netty.channel.ChannelFutureListener listener=x->{
            if(x.isSuccess()){
                x.channel().writeAndFlush(msg);
                this.remoteChannel =x.channel();
            }else{
                throw  new RuntimeException("remoteChannel连接失败");
            }
        };
        this.remoteChannelFuture.addListener(listener);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        this.remoteChannel.config().setAutoRead(this.remoteChannel.isWritable());
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
        this.remoteChannel.close();
    }
}
